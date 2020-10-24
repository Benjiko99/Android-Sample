package com.spiraclesoftware.androidsample.ui.transactiondetail

import co.zsmb.rainbowcake.withIOContext
import com.spiraclesoftware.androidsample.data.network.model.TransactionUpdateRequest
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.domain.model.TransactionId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

class TransactionDetailPresenter(
    private val transactionsInteractor: TransactionsInteractor
) {

    suspend fun getTransactionById(id: TransactionId) = withIOContext {
        transactionsInteractor.getTransactionById(id)
    }

    fun flowTransactionById(id: TransactionId): Flow<Transaction?> {
        return transactionsInteractor.flowTransactionById(id).distinctUntilChanged()
    }

    suspend fun getNote(id: TransactionId) = withIOContext {
        getTransactionById(id)?.noteToSelf.orEmpty()
    }

    suspend fun getCategory(id: TransactionId) = withIOContext {
        getTransactionById(id)?.category
    }

    suspend fun updateNote(id: TransactionId, note: String) = withIOContext {
        val request = TransactionUpdateRequest(noteToSelf = note)
        transactionsInteractor.updateTransaction(id, request)
    }

    suspend fun removeAttachment(id: TransactionId, url: String) = withIOContext {
        transactionsInteractor.removeAttachment(id, url)
    }

}
