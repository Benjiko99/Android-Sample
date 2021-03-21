package com.spiraclesoftware.androidsample.domain

import com.spiraclesoftware.androidsample.domain.entity.*
import kotlinx.coroutines.flow.Flow
import java.util.*

interface LocalDataSource {
    fun getAccount(): Account

    fun getProfile(): Profile

    fun flowTransactions(): Flow<List<Transaction>>

    fun getTransactionById(id: TransactionId): Transaction?

    fun flowTransactionById(id: TransactionId): Flow<Transaction?>

    fun saveTransactions(transactions: List<Transaction>)

    fun clearTransactions()

    fun updateTransaction(transaction: Transaction)

    fun updateTransaction(id: TransactionId, update: (Transaction) -> Transaction)

    fun saveConversionRates(baseCurrency: Currency, rates: ConversionRates)

    fun getConversionRates(baseCurrency: Currency): ConversionRates?
}