package com.spiraclesoftware.androidsample.features.transaction_list

import co.zsmb.rainbowcake.withIOContext
import com.mikepenz.fastadapter.GenericItem
import com.spiraclesoftware.androidsample.domain.Result
import com.spiraclesoftware.androidsample.domain.interactor.AccountsInteractor
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import com.spiraclesoftware.androidsample.domain.model.Account
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.formatters.ExceptionFormatter
import com.spiraclesoftware.androidsample.framework.PresenterException
import com.spiraclesoftware.androidsample.utils.LanguageManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
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

    suspend fun refreshTransactions() = withIOContext {
        transactionsInteractor.refreshTransactions()
    }

    suspend fun flowTransactions(
        listFilter: Flow<TransactionListFilter>
    ) = withIOContext {
        transactionsInteractor.flowTransactions()
            .map(::formatErrorResult)
            .combine(listFilter) { result, filter ->
                when (result) {
                    is Result.Success -> Result.Success(filter.applyTo(result.data))
                    else -> result
                }
            }
    }

    private fun <T> formatErrorResult(result: Result<T>): Result<T> {
        return when (result) {
            is Result.Error -> {
                Timber.e(result.exception)
                val message = exceptionFormatter.format(result.exception)
                Result.Error(PresenterException(message))
            }
            else -> result
        }
    }

    suspend fun getListItems(transactions: List<Transaction>): List<GenericItem> {
        return transactions
            .sortAndGroupByDay()
            .toListItems()
    }

    private fun List<Transaction>.sortAndGroupByDay() =
        this.sortedByDescending { it.processingDate }
            .groupBy { it.processingDate.truncatedTo(DAYS) }

    private suspend fun Map<ZonedDateTime, List<Transaction>>.toListItems(): List<GenericItem> {
        val listItems = arrayListOf<GenericItem>()

        this.forEach { (day, transactions) ->
            val contribution = accountsInteractor.getContributionToBalance(transactions)

            listItems += HeaderItem(day, contribution)
            listItems += transactions.map(::TransactionItem)
        }
        return listItems
    }

}