package com.spiraclesoftware.androidsample.ui.transactiondetail

import co.zsmb.rainbowcake.withIOContext
import com.spiraclesoftware.androidsample.shared.domain.Transaction
import com.spiraclesoftware.androidsample.shared.domain.TransactionId
import com.spiraclesoftware.androidsample.shared.domain.TransactionsInteractor

class TransactionDetailPresenter(
    private val transactionsInteractor: TransactionsInteractor
) {

    suspend fun getTransactionById(id: TransactionId): Transaction? = withIOContext {
        transactionsInteractor.getTransactionById(id)
    }

}
