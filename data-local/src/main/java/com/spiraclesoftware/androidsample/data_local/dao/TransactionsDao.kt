package com.spiraclesoftware.androidsample.data_local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.spiraclesoftware.androidsample.data_local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionsDao :
    BaseDao<TransactionEntity> {

    @Query("SELECT * FROM transactions")
    fun getAll(): List<TransactionEntity>

    @Query("SELECT * FROM transactions")
    fun flowAll(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    fun getById(id: String): TransactionEntity?

    @Query("SELECT * FROM transactions WHERE id = :id")
    fun flowById(id: String): Flow<TransactionEntity?>

    @Query("DELETE FROM transactions")
    fun deleteAll()

    @Transaction
    fun repopulateWith(obj: List<TransactionEntity>) {
        deleteAll()
        insertAll(obj)
    }

}