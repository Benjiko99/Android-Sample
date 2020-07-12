package com.spiraclesoftware.androidsample.shared.data.disk

import com.spiraclesoftware.androidsample.shared.domain.Transaction
import com.spiraclesoftware.androidsample.shared.domain.TransactionId
import com.spiraclesoftware.core.data.AssociatedListCache

class DiskDataSource(
    private val listCache: AssociatedListCache<TransactionId, Transaction>
) {

    fun saveTransactions(transactions: List<Transaction>) {
        return listCache.set(transactions)
    }

    fun getTransactions(): List<Transaction>? {
        return listCache.get()
    }

    fun getTransactionById(id: TransactionId): Transaction? {
        return listCache.get(id)
    }

}
