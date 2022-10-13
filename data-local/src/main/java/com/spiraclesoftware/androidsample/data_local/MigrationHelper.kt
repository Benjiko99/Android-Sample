package com.spiraclesoftware.androidsample.data_local

import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Helps with writing migrations in a declarative fashion.
 *
 * https://gist.github.com/Benjiko99/23fbeee37d1d9f9a8b52ad21fc2585b9
 */
open class MigrationHelper {

    /**
     * Helper for altering tables.
     * Handles creation of a temp table and copying its contents to the new table.
     *
     * Supported operations: Add, Delete, Rename or Change Scheme of any existing column.
     *
     *  To Retain an existing column, it has to be defined in [columns] otherwise it will be Deleted.
     *
     * To Add a new column or Retain an existing column, use one of:
     *  - `"foo INTEGER".toNothing()                        // Add/Retain column with null value (or default value if defined in schema).`
     *  - `"foo INTEGER".toExisting()                       // Retain column with its existing value from previous version of this table.`
     *  - `"foo INTEGER".toExisting("bar")                  // Add/Retain column with a value from another existing column.`
     *  - `"foo INTEGER".toExisting("COALESCE(bar, baz)")   // Add/Retain column with a value from the first non-null column in the COALESCE statement.`
     *
     * To Delete a column, omit it from the [columns] map.
     *
     * To Rename a column, use: `"foo INTEGER".toExisting("bar")`, which will map the value of `bar` column to the `foo` column.
     *
     * To Change Scheme of a column, specify the new scheme in the [columns]' key, e.g:
     *  - `"foo TEXT NOT NULL".toExisting("bar")
     */
    protected fun alterTable(
        db: SupportSQLiteDatabase,
        tableName: String,
        columns: Map<String, String?>,
        primaryKeys: List<String>
    ) {
        val createTempTable = "CREATE TABLE ${tableName}_temp (" + columns.map { it.key }
            .joinToString() + ", PRIMARY KEY(${primaryKeys.joinToString()}))"
        db.execSQL(createTempTable)

        /** Filters only columns that want to mapped to another column. */
        val columnsWithMapping = columns.filterValues { it != null }

        val columnNames = columnsWithMapping.map { it.key.substringBefore(' ') }.joinToString()
        val insertIntoTempTable = "INSERT INTO ${tableName}_temp (" + columnNames + ") "
        val selectFromRealTable = "SELECT " + columnsWithMapping.map { it.value }.joinToString() + " FROM $tableName"
        db.execSQL(insertIntoTempTable + selectFromRealTable)

        db.execSQL("DROP TABLE $tableName")
        db.execSQL("ALTER TABLE ${tableName}_temp RENAME TO $tableName")
    }

    /**
     * Indicates that this column should copy the value from an existing column of this table.
     * If [column] is null, the value will be copied from [this] column.
     * If a [column] is specified, value will be copied from it.
     */
    protected fun String.toExisting(column: String? = null): Pair<String, String> =
        Pair(this, column ?: this.substringBefore(' '))

    /**
     * Creates a pairing to a null value, indicating there is no previous column to copy a value from.
     * Used when adding new columns.
     * */
    protected fun String.toNothing(): Pair<String, Nothing?> = Pair(this, null)
}