package com.spiraclesoftware.androidsample.domain.interactor

import android.net.Uri
import com.spiraclesoftware.androidsample.data.disk.DiskDataSource
import com.spiraclesoftware.androidsample.data.network.NetworkDataSource
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.domain.model.TransactionCategory
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

    suspend fun updateTransactionNote(id: TransactionId, note: String) =
        networkDataSource.updateTransactionNote(id, note).also {
            diskDataSource.updateTransaction(it)
        }

    suspend fun updateTransactionCategory(id: TransactionId, category: TransactionCategory) =
        networkDataSource.updateTransactionCategory(id, category).also {
            diskDataSource.updateTransaction(it)
        }

    suspend fun uploadAttachment(id: TransactionId, uri: Uri) {
        networkDataSource.uploadAttachment(id, uri).also { uri ->
            diskDataSource.updateTransaction(id) {
                it.copy(attachments = it.attachments.plus(uri))
            }
        }
    }

    suspend fun removeAttachment(id: TransactionId, uri: Uri) {
        networkDataSource.removeAttachment(id, uri).also {
            diskDataSource.updateTransaction(id) {
                it.copy(attachments = it.attachments.minus(uri))
            }
        }
    }

}
