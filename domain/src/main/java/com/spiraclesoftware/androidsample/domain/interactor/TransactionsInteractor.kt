package com.spiraclesoftware.androidsample.domain.interactor

import com.spiraclesoftware.androidsample.domain.LocalDataSource
import com.spiraclesoftware.androidsample.domain.RemoteDataSource
import com.spiraclesoftware.androidsample.domain.Result
import com.spiraclesoftware.androidsample.domain.entity.Transaction
import com.spiraclesoftware.androidsample.domain.entity.TransactionCategory
import com.spiraclesoftware.androidsample.domain.entity.TransactionId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach

class TransactionsInteractor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) {

    private val remoteDataSourceResult = MutableStateFlow<Result<List<Transaction>>>(Result.Loading)

    fun flowTransactions() = localDataSource.flowTransactions()
        // includes errors and loading state from remoteDataSource
        .combine(remoteDataSourceResult) { localData, remoteResult ->
            when (remoteResult) {
                is Result.Success -> Result.Success(localData)
                is Result.Error -> remoteResult
                is Result.Loading -> remoteResult
            }
        }

    suspend fun refreshTransactions() {
        remoteDataSource.fetchTransactions()
            .onEach { result ->
                when (result) {
                    is Result.Success -> localDataSource.saveTransactions(result.data)
                    is Result.Error -> localDataSource.clearTransactions()
                }
            }
            .collect { result -> remoteDataSourceResult.value = result }
    }

    fun getTransactionById(id: TransactionId): Transaction? {
        return localDataSource.getTransactionById(id)
    }

    fun flowTransactionById(id: TransactionId) =
        localDataSource.flowTransactionById(id)

    suspend fun updateTransactionNote(id: TransactionId, note: String) =
        remoteDataSource.updateTransactionNote(id.value, note).also {
            localDataSource.updateTransaction(it)
        }

    suspend fun updateTransactionCategory(id: TransactionId, category: TransactionCategory) =
        remoteDataSource.updateTransactionCategory(id.value, category.name).also {
            localDataSource.updateTransaction(it)
        }

    suspend fun uploadAttachment(id: TransactionId, uri: String) {
        remoteDataSource.uploadAttachment(id.value, uri).also { attachment ->
            localDataSource.updateTransaction(id) {
                it.copy(attachments = it.attachments.plus(attachment))
            }
        }
    }

    suspend fun removeAttachment(id: TransactionId, uri: String) {
        remoteDataSource.removeAttachment(id.value, uri).also {
            localDataSource.updateTransaction(id) {
                it.copy(attachments = it.attachments.minus(uri))
            }
        }
    }

    fun getAllCategories(): List<TransactionCategory> {
        return TransactionCategory.values().toList()
    }

}
