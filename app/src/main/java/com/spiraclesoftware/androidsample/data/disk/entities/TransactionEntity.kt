package com.spiraclesoftware.androidsample.data.disk.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.spiraclesoftware.androidsample.domain.model.*
import org.threeten.bp.ZonedDateTime

@Entity(tableName = "transactions")
class TransactionEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val processingDate: String,
    @Embedded(prefix = "money")
    val money: Money,
    val transferDirection: TransferDirection,
    val category: TransactionCategory,
    val status: TransactionStatus,
    val statusCode: TransactionStatusCode,
    val cardDescription: String? = null,
    val noteToSelf: String? = null
)

fun TransactionEntity.toDomain(): Transaction {
    return Transaction(
        id = TransactionId(id),
        name = name,
        processingDate = ZonedDateTime.parse(processingDate),
        money = money,
        transferDirection = transferDirection,
        category = category,
        status = status,
        statusCode = statusCode,
        cardDescription = cardDescription,
        noteToSelf = noteToSelf
    )
}

fun Transaction.toRoomEntity(): TransactionEntity {
    return TransactionEntity(
        id = id.value,
        name = name,
        processingDate = processingDate.toString(),
        money = money,
        transferDirection = transferDirection,
        category = category,
        status = status,
        statusCode = statusCode,
        cardDescription = cardDescription,
        noteToSelf = noteToSelf
    )
}