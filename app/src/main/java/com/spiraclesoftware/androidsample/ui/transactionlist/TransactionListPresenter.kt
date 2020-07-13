package com.spiraclesoftware.androidsample.ui.transactionlist

import co.zsmb.rainbowcake.withIOContext
import com.mikepenz.fastadapter.GenericItem
import com.spiraclesoftware.androidsample.domain.interactor.AccountsInteractor
import com.spiraclesoftware.androidsample.domain.interactor.ConversionRatesInteractor
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import com.spiraclesoftware.androidsample.domain.model.*
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.temporal.ChronoUnit

class TransactionListPresenter(
    private val accountsInteractor: AccountsInteractor,
    private val transactionsInteractor: TransactionsInteractor,
    private val conversionRatesInteractor: ConversionRatesInteractor
) {

    suspend fun getAccount(): Account = withIOContext {
        accountsInteractor.getAccount()
    }

    suspend fun getConversionRates(ignoreCached: Boolean = false): ConversionRates = withIOContext {
        conversionRatesInteractor.getConversionRates(getAccount().currency.currencyCode(), ignoreCached)
    }

    suspend fun getTransactions(
        filter: TransactionListFilter,
        ignoreCached: Boolean = false
    ): List<Transaction> = withIOContext {
        transactionsInteractor.getTransactions(ignoreCached)
            .applyFilter(filter)
    }

    suspend fun getListItems(
        filter: TransactionListFilter,
        ignoreCached: Boolean = false
    ): List<GenericItem> = withIOContext {
        getTransactions(filter, ignoreCached)
            .sortAndGroupByDay()
            .toListItems(getAccount(), getConversionRates(ignoreCached))
    }

    private fun List<Transaction>.sortAndGroupByDay() =
        this.sortedByDescending { it.processingDate }
            .groupBy { it.processingDate.truncatedTo(ChronoUnit.DAYS) }

    private fun Map<ZonedDateTime, List<Transaction>>.toListItems(
        account: Account,
        rates: ConversionRates
    ): List<GenericItem> {
        val listItems = arrayListOf<GenericItem>()

        this.forEach { (day, transactions) ->
            val contributions = transactions.getContributionsToBalance(rates, account.currency)

            listItems.add(HeaderItem(day, contributions))
            listItems.addAll(transactions.map(::TransactionItem))
        }

        return listItems
    }

}