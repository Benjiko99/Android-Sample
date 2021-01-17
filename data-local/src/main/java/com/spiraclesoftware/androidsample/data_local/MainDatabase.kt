package com.spiraclesoftware.androidsample.data_local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.spiraclesoftware.androidsample.data_local.dao.TransactionsDao
import com.spiraclesoftware.androidsample.data_local.entities.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class MainDatabase : RoomDatabase() {

    abstract fun transactionsDao(): TransactionsDao

    object Migrations : MigrationHelper() {

        val ALL_MIGRATIONS get() = arrayOf<Migration>()

        /** Not a real migration. just an example */
        val MIGRATION_EXAMPLE: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                /**
                 * Handles creation of a temp table and copying its contents to the new table.
                 * Supported operations: Add, Delete, Rename or Change Scheme of any existing column.
                 */
                alterTable(
                    db = database,
                    tableName = "transactions",
                    columns = mapOf(
                        "id INTEGER".toExisting(),                  // Retains column "id" without changes
                        "title TEXT".toExisting("name"),    // Renames column "name" to "title"
                        "noteForRecipient TEXT".toNothing(),        // Adds a new column "noteForRecipient"
                        "processingDate TEXT NOT NULL".toExisting() // Changes scheme of column "processingDate", adding "NOT NULL"
                        // Any columns that existed in the old table
                        // but aren't specified in this map will be deleted!
                    ),
                    primaryKeys = listOf("id") // Add more than one key to create a compound primary key
                )
            }
        }

    }
}
