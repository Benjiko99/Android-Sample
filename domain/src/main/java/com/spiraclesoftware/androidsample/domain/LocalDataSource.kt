package com.spiraclesoftware.androidsample.domain

import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.domain.model.TransactionId
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun flowTransactions(): Flow<List<Transaction>>

    fun getTransactionById(id: TransactionId): Transaction?

    fun flowTransactionById(id: TransactionId): Flow<Transaction?>

    fun saveTransactions(transactions: List<Transaction>)

    fun updateTransaction(transaction: Transaction)

    fun updateTransaction(id: TransactionId, update: (Transaction) -> Transaction)
}