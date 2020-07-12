package com.spiraclesoftware.androidsample.shared.domain

import com.spiraclesoftware.androidsample.shared.data.disk.DiskDataSource
import com.spiraclesoftware.androidsample.shared.data.network.NetworkDataSource

class TransactionsInteractor(
    private val networkDataSource: NetworkDataSource,
    private val diskDataSource: DiskDataSource
) {

    suspend fun getTransactions(): List<Transaction> {
        return diskDataSource.getTransactions() ?: networkDataSource.getTransactions().also {
            diskDataSource.saveTransactions(it)
        }
    }

    suspend fun getTransactionById(id: TransactionId): Transaction? {
        return diskDataSource.getTransactionById(id) ?: networkDataSource.getTransactions().run {
            diskDataSource.saveTransactions(this)
            this.find { it.id == id }
        }
    }

}
