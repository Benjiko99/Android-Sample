package com.spiraclesoftware.androidsample.domain.interactor

import com.spiraclesoftware.androidsample.data.disk.DiskDataSource
import com.spiraclesoftware.androidsample.data.network.NetworkDataSource
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.domain.model.TransactionId

class TransactionsInteractor(
    private val networkDataSource: NetworkDataSource,
    private val diskDataSource: DiskDataSource
) {

    suspend fun getTransactions(ignoreCached: Boolean = false): List<Transaction> {
        suspend fun getFromNetwork() =
            networkDataSource.getTransactions().also {
                diskDataSource.saveTransactions(it)
            }

        return if (ignoreCached) getFromNetwork()
        else diskDataSource.getTransactions() ?: getFromNetwork()
    }

    suspend fun getTransactionById(id: TransactionId): Transaction? {
        return diskDataSource.getTransactionById(id) ?: networkDataSource.getTransactions().run {
            diskDataSource.saveTransactions(this)
            this.find { it.id == id }
        }
    }

}
