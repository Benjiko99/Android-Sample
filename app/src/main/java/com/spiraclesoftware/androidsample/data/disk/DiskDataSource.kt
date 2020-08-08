package com.spiraclesoftware.androidsample.data.disk

import com.spiraclesoftware.androidsample.data.disk.dao.TransactionsDao
import com.spiraclesoftware.androidsample.data.disk.entities.TransactionEntity
import com.spiraclesoftware.androidsample.data.disk.entities.toDomain
import com.spiraclesoftware.androidsample.data.disk.entities.toRoomEntity
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.domain.model.TransactionId

class DiskDataSource(
    private val transactionsDao: TransactionsDao
) {

    fun getTransactions(): List<Transaction> {
        val roomItems = transactionsDao.getAll()
        return roomItems.map(TransactionEntity::toDomain)
    }

    fun getTransactionById(id: TransactionId): Transaction? {
        val roomItem = transactionsDao.getById(id.value)
        return roomItem?.let(TransactionEntity::toDomain)
    }

    fun saveTransactions(transactions: List<Transaction>) {
        val roomItems = transactions.map(Transaction::toRoomEntity)
        return transactionsDao.insertAll(roomItems)
    }

    fun updateTransaction(transaction: Transaction) {
        val item = transaction.let(Transaction::toRoomEntity)
        return transactionsDao.update(item)
    }

}
