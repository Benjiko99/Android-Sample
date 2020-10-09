package com.spiraclesoftware.androidsample.domain.interactor

import com.spiraclesoftware.androidsample.data.disk.DiskDataSource
import com.spiraclesoftware.androidsample.data.network.NetworkDataSource
import com.spiraclesoftware.androidsample.data.network.model.TransactionUpdateRequest
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.domain.model.TransactionId

class TransactionsInteractor(
    private val networkDataSource: NetworkDataSource,
    private val diskDataSource: DiskDataSource
) {

    suspend fun fetchTransactions() =
        networkDataSource.fetchTransactions().also {
            diskDataSource.saveTransactions(it)
        }

    fun flowTransactions() =
        diskDataSource.flowTransactions()

    suspend fun getTransactionById(id: TransactionId): Transaction? {
        val cached = diskDataSource.getTransactionById(id)
        return cached ?: fetchTransactions().run { find { it.id == id } }
    }

    fun flowTransactionById(id: TransactionId) =
        diskDataSource.flowTransactionById(id)

    suspend fun updateTransaction(id: TransactionId, request: TransactionUpdateRequest) =
        networkDataSource.updateTransaction(id, request).also {
            diskDataSource.updateTransaction(it)
        }

}
