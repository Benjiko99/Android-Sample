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

}
