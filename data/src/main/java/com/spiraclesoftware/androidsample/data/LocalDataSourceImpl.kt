package com.spiraclesoftware.androidsample.data

import com.spiraclesoftware.androidsample.data.mapper.MoneyEntityMapper
import com.spiraclesoftware.androidsample.data.mapper.TransactionEntityMapper
import com.spiraclesoftware.androidsample.data_local.dao.TransactionsDao
import com.spiraclesoftware.androidsample.domain.LocalDataSource
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.domain.model.TransactionId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalDataSourceImpl(
    private val transactionsDao: TransactionsDao
) : LocalDataSource {

    private val moneyEntityMapper = MoneyEntityMapper()
    private val transactionEntityMapper = TransactionEntityMapper(moneyEntityMapper)

    override fun flowTransactions(): Flow<List<Transaction>> {
        val roomItems = transactionsDao.flowAll()
        return roomItems.map { it.map(transactionEntityMapper::mapToDomain) }
    }

    override fun getTransactionById(id: TransactionId): Transaction? {
        val roomItem = transactionsDao.getById(id.value)
        return roomItem?.let(transactionEntityMapper::mapToDomain)
    }

    override fun flowTransactionById(id: TransactionId): Flow<Transaction?> {
        val roomItem = transactionsDao.flowById(id.value)
        return roomItem.map { it?.let(transactionEntityMapper::mapToDomain) }
    }

    override fun saveTransactions(transactions: List<Transaction>) {
        val roomItems = transactions.map(transactionEntityMapper::mapToEntity)
        transactionsDao.repopulateWith(roomItems)
    }

    override fun updateTransaction(transaction: Transaction) {
        val item = transaction.let(transactionEntityMapper::mapToEntity)
        transactionsDao.update(item)
    }

    override fun updateTransaction(id: TransactionId, update: (Transaction) -> Transaction) {
        getTransactionById(id)?.let {
            updateTransaction(update(it))
        }
    }

}
