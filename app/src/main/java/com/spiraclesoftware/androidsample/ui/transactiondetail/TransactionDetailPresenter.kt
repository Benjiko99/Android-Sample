package com.spiraclesoftware.androidsample.ui.transactiondetail

import co.zsmb.rainbowcake.withIOContext
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.domain.model.TransactionId
import com.spiraclesoftware.androidsample.domain.policy.TransactionsPolicy

class TransactionDetailPresenter(
    private val transactionsInteractor: TransactionsInteractor
) {

    fun isSuccessful(transaction: Transaction): Boolean {
        return TransactionsPolicy.isSuccessful(transaction)
    }

    fun contributesToBalance(transaction: Transaction): Boolean {
        return TransactionsPolicy.contributesToBalance(transaction)
    }

    suspend fun getTransactionById(id: TransactionId): Transaction? = withIOContext {
        transactionsInteractor.getTransactionById(id)
    }

    suspend fun getNote(id: TransactionId): String {
        return getTransactionById(id)?.noteToSelf.orEmpty()
    }

    suspend fun updateNote(id: TransactionId, note: String) = withIOContext {
        val noteOrNull = if (note.isBlank()) null else note
        transactionsInteractor.updateNote(id, noteOrNull)
    }

}
