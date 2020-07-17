package com.spiraclesoftware.androidsample.ui.transactionlist

import co.zsmb.rainbowcake.withIOContext
import com.mikepenz.fastadapter.GenericItem
import com.spiraclesoftware.androidsample.domain.interactor.AccountsInteractor
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import com.spiraclesoftware.androidsample.domain.model.Account
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.domain.model.TransactionListFilter
import com.spiraclesoftware.androidsample.domain.model.applyFilter
import com.spiraclesoftware.androidsample.domain.policy.TransactionsPolicy
import com.spiraclesoftware.core.utils.LanguageManager
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.temporal.ChronoUnit

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

    suspend fun getTransactions(
        filter: TransactionListFilter,
        forceFetch: Boolean = false
    ): List<Transaction> = withIOContext {
        val transactions = if (forceFetch)
            transactionsInteractor.fetchTransactions()
        else
            transactionsInteractor.getTransactions()

        transactions.applyFilter(filter)
    }

    suspend fun getListItems(
        transactions: List<Transaction>
    ): List<GenericItem> = withIOContext {
        transactions
            .sortAndGroupByDay()
            .toListItems()
    }

    private fun List<Transaction>.sortAndGroupByDay() =
        this.sortedByDescending { it.processingDate }
            .groupBy { it.processingDate.truncatedTo(ChronoUnit.DAYS) }

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