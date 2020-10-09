package com.spiraclesoftware.androidsample.data.disk

import com.spiraclesoftware.androidsample.data.disk.dao.TransactionsDao
import com.spiraclesoftware.androidsample.data.disk.entities.TransactionEntity
import com.spiraclesoftware.androidsample.data.disk.entities.toDomain
import com.spiraclesoftware.androidsample.data.disk.entities.toRoomEntity
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.domain.model.TransactionId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DiskDataSource(
    private val transactionsDao: TransactionsDao
) {

    fun flowTransactions(): Flow<List<Transaction>> {
        val roomItems = transactionsDao.flowAll()
        return roomItems.map { it.map(TransactionEntity::toDomain) }
    }

    fun getTransactionById(id: TransactionId): Transaction? {
        val roomItem = transactionsDao.getById(id.value)
        return roomItem?.let(TransactionEntity::toDomain)
    }

    fun flowTransactionById(id: TransactionId): Flow<Transaction?> {
        val roomItem = transactionsDao.flowById(id.value)
        return roomItem.map { it?.toDomain() }
    }

    fun saveTransactions(transactions: List<Transaction>) {
        val roomItems = transactions.map(Transaction::toRoomEntity)
        transactionsDao.replaceAll(roomItems)
    }

    fun updateTransaction(transaction: Transaction) {
        val item = transaction.let(Transaction::toRoomEntity)
        transactionsDao.update(item)
    }

}
