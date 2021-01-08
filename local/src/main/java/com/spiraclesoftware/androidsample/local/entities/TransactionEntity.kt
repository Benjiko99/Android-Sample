package com.spiraclesoftware.androidsample.local.entities

import android.net.Uri
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
    val attachments: List<Uri> = emptyList(),
    val cardDescription: String? = null,
    val noteToSelf: String? = null
)
