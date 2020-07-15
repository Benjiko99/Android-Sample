package com.spiraclesoftware.androidsample.ui.transactionlist

import co.zsmb.rainbowcake.withIOContext
import com.mikepenz.fastadapter.GenericItem
import com.spiraclesoftware.androidsample.domain.interactor.AccountsInteractor
import com.spiraclesoftware.androidsample.domain.interactor.ConversionRatesInteractor
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import com.spiraclesoftware.androidsample.domain.model.*
import com.spiraclesoftware.core.utils.LanguageManager
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.temporal.ChronoUnit

class TransactionListPresenter(
    private val languageManager: LanguageManager,
    private val accountsInteractor: AccountsInteractor,
    private val transactionsInteractor: TransactionsInteractor,
    private val conversionRatesInteractor: ConversionRatesInteractor
) {

    fun toggleLanguageAndRestart() {
        languageManager.toggleLanguageAndRestart()
    }

    suspend fun getAccount(): Account = withIOContext {
        accountsInteractor.getAccount()
    }

    suspend fun getConversionRates(ignoreCache: Boolean = false): ConversionRates = withIOContext {
        val baseCurrency = getAccount().currency.currencyCode()
        if (ignoreCache)
            conversionRatesInteractor.fetchConversionRates(baseCurrency)
        else
            conversionRatesInteractor.getConversionRates(baseCurrency)
    }

    suspend fun getTransactions(
        filter: TransactionListFilter,
        ignoreCache: Boolean = false
    ): List<Transaction> = withIOContext {
        val transactions = if (ignoreCache)
            transactionsInteractor.fetchTransactions()
        else
            transactionsInteractor.getTransactions()

        transactions.applyFilter(filter)
    }

    suspend fun getListItems(
        filter: TransactionListFilter,
        ignoreCache: Boolean = false
    ): List<GenericItem> = withIOContext {
        getTransactions(filter, ignoreCache)
            .sortAndGroupByDay()
            .toListItems(getAccount(), getConversionRates(ignoreCache))
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