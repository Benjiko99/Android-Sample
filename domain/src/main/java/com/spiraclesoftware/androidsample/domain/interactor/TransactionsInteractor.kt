package com.spiraclesoftware.androidsample.domain.interactor

import android.net.Uri
import com.spiraclesoftware.androidsample.domain.DiskDataSource
import com.spiraclesoftware.androidsample.domain.NetworkDataSource
import com.spiraclesoftware.androidsample.domain.Result
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.domain.model.TransactionCategory
import com.spiraclesoftware.androidsample.domain.model.TransactionId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine

class TransactionsInteractor(
    private val networkDataSource: NetworkDataSource,
    private val diskDataSource: DiskDataSource
) {

    private val networkDataSourceResult = MutableStateFlow<Result<List<Transaction>>>(Result.Loading)

    fun flowTransactions() = diskDataSource.flowTransactions()
        // includes errors and loading state from networkDataSource
        .combine(networkDataSourceResult) { diskData, networkResult ->
            when (networkResult) {
                is Result.Success -> Result.Success(diskData)
                is Result.Error -> networkResult
                is Result.Loading -> networkResult
            }
        }

    suspend fun refreshTransactions() {
        networkDataSource.fetchTransactions().collect { result ->
            if (result is Result.Success) {
                diskDataSource.saveTransactions(result.data)
            }
            networkDataSourceResult.value = result
        }
    }

    fun getTransactionById(id: TransactionId): Transaction? {
        return diskDataSource.getTransactionById(id)
    }

    fun flowTransactionById(id: TransactionId) =
        diskDataSource.flowTransactionById(id)

    suspend fun updateTransactionNote(id: TransactionId, note: String) =
        networkDataSource.updateTransactionNote(id.value, note).also {
            diskDataSource.updateTransaction(it)
        }

    suspend fun updateTransactionCategory(id: TransactionId, category: TransactionCategory) =
        networkDataSource.updateTransactionCategory(id.value, category.name).also {
            diskDataSource.updateTransaction(it)
        }

    suspend fun uploadAttachment(id: TransactionId, uri: Uri) {
        networkDataSource.uploadAttachment(id.value, uri).also { attachment ->
            diskDataSource.updateTransaction(id) {
                it.copy(attachments = it.attachments.plus(attachment))
            }
        }
    }

    suspend fun removeAttachment(id: TransactionId, uri: Uri) {
        networkDataSource.removeAttachment(id.value, uri).also {
            diskDataSource.updateTransaction(id) {
                it.copy(attachments = it.attachments.minus(uri))
            }
        }
    }

}
