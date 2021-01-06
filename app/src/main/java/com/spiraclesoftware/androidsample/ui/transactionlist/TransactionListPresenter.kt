package com.spiraclesoftware.androidsample.ui.transactionlist

import co.zsmb.rainbowcake.withIOContext
import com.mikepenz.fastadapter.GenericItem
import com.spiraclesoftware.androidsample.domain.interactor.AccountsInteractor
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import com.spiraclesoftware.androidsample.domain.model.Account
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.domain.model.TransactionListFilter
import com.spiraclesoftware.androidsample.domain.policy.CurrencyConverter
import com.spiraclesoftware.androidsample.ui.shared.ExceptionFormatter
import com.spiraclesoftware.androidsample.ui.shared.PresenterException
import com.spiraclesoftware.core.utils.LanguageManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.temporal.ChronoUnit.DAYS
import timber.log.Timber

class TransactionListPresenter(
    private val languageManager: LanguageManager,
    private val accountsInteractor: AccountsInteractor,
    private val transactionsInteractor: TransactionsInteractor,
    private val exceptionFormatter: ExceptionFormatter,
) {

    fun toggleLanguageAndRestart() {
        languageManager.toggleLanguageAndRestart()
    }

    suspend fun getAccount(): Account = withIOContext {
        accountsInteractor.getAccount()
    }

    @Throws(PresenterException::class)
    suspend fun fetchTransactions() = withIOContext {
        try {
            transactionsInteractor.fetchTransactions()
        } catch (e: Exception) {
            Timber.e(e); throw PresenterException(exceptionFormatter.format(e))
        }
    }

    fun flowFilteredTransactions(
        listFilter: Flow<TransactionListFilter>
    ) = transactionsInteractor
        .flowTransactions()
        .combine(listFilter) { list, filter ->
            filter.applyTo(list)
        }

    @Throws(PresenterException::class)
    suspend fun getListItems(transactions: List<Transaction>) =
        transactions
            .sortAndGroupByDay()
            .toListItems()

    private fun List<Transaction>.sortAndGroupByDay() =
        this.sortedByDescending { it.processingDate }
            .groupBy { it.processingDate.truncatedTo(DAYS) }

    private suspend fun Map<ZonedDateTime, List<Transaction>>.toListItems(): List<GenericItem> {
        val listItems = arrayListOf<GenericItem>()

        this.forEach { (day, transactions) ->
            val contribution = getContributionToBalance(transactions)

            listItems += HeaderItem(day, contribution)
            listItems += transactions.map(::TransactionItem)
        }
        return listItems
    }

    private suspend fun getContributionToBalance(transactions: List<Transaction>) =
        try {
            accountsInteractor.getContributionToBalance(transactions)
        } catch (e: CurrencyConverter.MissingConversionRateException) {
            Timber.e(e); throw PresenterException(exceptionFormatter.format(e))
        }

}