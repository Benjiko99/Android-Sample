package com.spiraclesoftware.androidsample.ui.transactiondetail

import co.zsmb.rainbowcake.withIOContext
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.domain.model.TransactionId

class TransactionDetailPresenter(
    private val transactionsInteractor: TransactionsInteractor
) {

    suspend fun getTransactionById(id: TransactionId): Transaction? = withIOContext {
        transactionsInteractor.getTransactionById(id)
    }

}
