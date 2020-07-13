package com.spiraclesoftware.androidsample.data.disk

import com.spiraclesoftware.androidsample.domain.model.*
import com.spiraclesoftware.core.data.disk.AssociatedItemCache
import com.spiraclesoftware.core.data.disk.AssociatedListCache
import java.util.*

class DiskDataSource {

    private val transactionsCache = AssociatedListCache<TransactionId, Transaction>()
    private val conversionRatesCache = AssociatedItemCache<CurrencyCode, ConversionRates>()
    private val dummyAccount = Account(Currency.getInstance("EUR"))

    fun getAccount(): Account {
        return dummyAccount
    }

    fun saveTransactions(transactions: List<Transaction>) {
        return transactionsCache.set(transactions)
    }

    fun getTransactions(): List<Transaction>? {
        return transactionsCache.get()
    }

    fun getTransactionById(id: TransactionId): Transaction? {
        return transactionsCache.get(id)
    }

    fun saveConversionRates(baseCurrency: CurrencyCode, rates: ConversionRates) {
        return conversionRatesCache.set(baseCurrency, rates)
    }

    fun getConversionRates(baseCurrency: CurrencyCode): ConversionRates? {
        return conversionRatesCache.get(baseCurrency)
    }

}