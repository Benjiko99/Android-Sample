package com.spiraclesoftware.androidsample.data_local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val processingDate: String,
    @Embedded(prefix = "money")
    val money: MoneyEntity,
    val transferDirection: String,
    val category: String,
    val status: String,
    val statusCode: String,
    val attachments: List<String> = emptyList(),
    val cardDescription: String? = null,
    val noteToSelf: String? = null
)
