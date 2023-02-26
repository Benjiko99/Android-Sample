package com.spiraclesoftware.androidsample.domain.interactor

import com.spiraclesoftware.androidsample.domain.LocalDataSource
import com.spiraclesoftware.androidsample.domain.RemoteDataSource
import com.spiraclesoftware.androidsample.domain.core.Result
import com.spiraclesoftware.androidsample.domain.core.mapOnSuccess
import com.spiraclesoftware.androidsample.domain.entity.Transaction
import com.spiraclesoftware.androidsample.domain.entity.TransactionCategory
import com.spiraclesoftware.androidsample.domain.entity.TransactionId
import com.spiraclesoftware.androidsample.domain.entity.TransactionsFilter
import kotlinx.coroutines.flow.*

class TransactionsInteractor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) {

    private val remoteDataSourceResult = MutableStateFlow<Result<List<Transaction>>>(Result.Loading)

    fun flowTransactions(filterFlow: Flow<TransactionsFilter>) =
        flowTransactions().combine(filterFlow) { result, filter ->
            result.mapOnSuccess { transactions ->
                Result.Success(filter.getFiltered(transactions))
            }
        }

    private fun flowTransactions(): Flow<Result<List<Transaction>>> =
        localDataSource.flowTransactions()
            // combine error and loading state from remoteDataSource
            // with data from localDataSource
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
                    Result.Loading -> {}
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
