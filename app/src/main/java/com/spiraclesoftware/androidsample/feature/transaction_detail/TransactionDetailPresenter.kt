package com.spiraclesoftware.androidsample.feature.transaction_detail

import android.net.Uri
import co.zsmb.rainbowcake.withIOContext
import com.spiraclesoftware.androidsample.domain.entity.Transaction
import com.spiraclesoftware.androidsample.domain.entity.TransactionId
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class TransactionDetailPresenter(
    private val transactionsInteractor: TransactionsInteractor,
    private val transactionDetailFormatter: TransactionDetailFormatter
) {

    suspend fun getTransactionById(id: TransactionId) = withIOContext {
        transactionsInteractor.getTransactionById(id)
    }
    
    fun flowDetailModel(id: TransactionId): Flow<DetailModel> {
        return flowTransactionById(id)
            .map { transaction ->
                transactionDetailFormatter.detailModel(transaction!!)
            }
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
        transactionsInteractor.updateTransactionNote(id, note)
    }

    suspend fun removeAttachment(id: TransactionId, uri: Uri) = withIOContext {
        transactionsInteractor.removeAttachment(id, uri.toString())
    }

    suspend fun uploadAttachment(id: TransactionId, uri: Uri) = withIOContext {
        transactionsInteractor.uploadAttachment(id, uri.toString())
    }

}
