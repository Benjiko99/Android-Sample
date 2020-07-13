package com.spiraclesoftware.androidsample.ui.transactionlist

import co.zsmb.rainbowcake.withIOContext
import com.spiraclesoftware.androidsample.shared.domain.*

class TransactionListPresenter(
    private val accountsInteractor: AccountsInteractor,
    private val transactionsInteractor: TransactionsInteractor,
    private val conversionRatesInteractor: ConversionRatesInteractor
) {

    suspend fun getAccount(): Account? = withIOContext {
        accountsInteractor.getAccount()
    }

    suspend fun getTransactions(
        filter: TransactionListFilter,
        ignoreCached: Boolean = false
    ): List<Transaction>? = withIOContext {
        transactionsInteractor.getTransactions(ignoreCached).applyFilter(filter)
    }

    suspend fun getConversionRates(ignoreCached: Boolean = false): ConversionRates? = withIOContext {
        getAccount()?.let { account ->
            conversionRatesInteractor.getConversionRates(account.currency.currencyCode(), ignoreCached)
        }
    }

}
