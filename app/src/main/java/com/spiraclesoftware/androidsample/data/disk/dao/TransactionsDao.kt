package com.spiraclesoftware.androidsample.data.disk.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.spiraclesoftware.androidsample.data.disk.entities.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionsDao :
    BaseDao<TransactionEntity> {

    @Query("SELECT * FROM transactions")
    fun flowAll(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    fun getById(id: Int): TransactionEntity?

    @Query("SELECT * FROM transactions WHERE id = :id")
    fun flowById(id: Int): Flow<TransactionEntity?>

    @Query("DELETE FROM transactions")
    fun deleteAll()

    @Transaction
    fun replaceAll(obj: List<TransactionEntity>) {
        deleteAll()
        insertAll(obj)
    }

}