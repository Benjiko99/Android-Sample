package com.spiraclesoftware.androidsample.ui.transactionlist

import co.zsmb.rainbowcake.withIOContext
import com.mikepenz.fastadapter.GenericItem
import com.spiraclesoftware.androidsample.domain.interactor.AccountsInteractor
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import com.spiraclesoftware.androidsample.domain.model.Account
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.domain.model.TransactionListFilter
import com.spiraclesoftware.androidsample.domain.policy.TransactionsPolicy
import com.spiraclesoftware.core.utils.LanguageManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.temporal.ChronoUnit.DAYS

class TransactionListPresenter(
    private val languageManager: LanguageManager,
    private val transactionsPolicy: TransactionsPolicy,
    private val accountsInteractor: AccountsInteractor,
    private val transactionsInteractor: TransactionsInteractor
) {

    fun toggleLanguageAndRestart() {
        languageManager.toggleLanguageAndRestart()
    }

    suspend fun getAccount(): Account = withIOContext {
        accountsInteractor.getAccount()
    }

    suspend fun fetchTransactions() = withIOContext {
        transactionsInteractor.fetchTransactions()
    }

    fun flowFilteredTransactions(
        listFilter: Flow<TransactionListFilter>
    ) = transactionsInteractor
        .flowTransactions()
        .combine(listFilter) { list, filter ->
            filter.applyTo(list)
        }

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
            val contribution = transactionsPolicy
                .getContributionToBalance(transactions, getAccount().currency)

            listItems.add(HeaderItem(day, contribution))
            listItems.addAll(transactions.map(::TransactionItem))
        }

        return listItems
    }

}