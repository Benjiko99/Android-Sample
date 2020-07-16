package com.spiraclesoftware.androidsample.data.disk.dao

import androidx.room.Dao
import androidx.room.Query
import com.spiraclesoftware.androidsample.data.disk.entities.TransactionEntity

@Dao
interface TransactionsDao :
    BaseDao<TransactionEntity> {

    @Query("SELECT * FROM transactions")
    fun getAll(): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE id = :id")
    fun getById(id: Int): TransactionEntity?

    @Query("DELETE FROM transactions")
    fun deleteAll()

}