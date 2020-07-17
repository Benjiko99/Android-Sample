package com.spiraclesoftware.androidsample.domain.interactor

import com.spiraclesoftware.androidsample.data.disk.DiskDataSource
import com.spiraclesoftware.androidsample.data.network.NetworkDataSource
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.domain.model.TransactionId

class TransactionsInteractor(
    private val networkDataSource: NetworkDataSource,
    private val diskDataSource: DiskDataSource
) {

    suspend fun fetchTransactions(): List<Transaction> {
        return networkDataSource.fetchTransactions().also {
            diskDataSource.saveTransactions(it)
        }
    }

    suspend fun getTransactions(): List<Transaction> {
        val cached = diskDataSource.getTransactions()
        return if (cached.isNotEmpty()) cached else fetchTransactions()
    }

    suspend fun getTransactionById(id: TransactionId): Transaction? {
        val cached = diskDataSource.getTransactionById(id)
        return cached ?: fetchTransactions().run { find { it.id == id } }
    }

}
