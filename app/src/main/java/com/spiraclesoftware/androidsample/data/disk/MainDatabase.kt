package com.spiraclesoftware.androidsample.data.disk

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.spiraclesoftware.androidsample.data.disk.dao.TransactionsDao
import com.spiraclesoftware.androidsample.data.disk.entities.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MainDatabase : RoomDatabase() {
    abstract fun transactionsDao(): TransactionsDao
}

