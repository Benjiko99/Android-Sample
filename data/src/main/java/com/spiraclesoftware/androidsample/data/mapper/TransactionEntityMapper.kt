package com.spiraclesoftware.androidsample.data.mapper

import com.spiraclesoftware.androidsample.domain.model.*
import com.spiraclesoftware.androidsample.local.entities.TransactionEntity
import org.threeten.bp.ZonedDateTime

class TransactionEntityMapper(
    private val moneyEntityMapper: MoneyEntityMapper
) : EntityMapper<TransactionEntity, Transaction> {

    override fun mapToDomain(obj: TransactionEntity): Transaction {
        return with(obj) {
            Transaction(
                id = TransactionId(id),
                name = name,
                processingDate = ZonedDateTime.parse(processingDate),
                money = moneyEntityMapper.mapToDomain(money),
                transferDirection = TransferDirection.valueOf(transferDirection),
                category = TransactionCategory.valueOf(category),
                status = TransactionStatus.valueOf(status),
                statusCode = TransactionStatusCode.valueOf(statusCode),
                attachments = attachments,
                cardDescription = cardDescription,
                noteToSelf = noteToSelf
            )
        }
    }

    override fun mapToEntity(obj: Transaction): TransactionEntity {
        return with(obj) {
            TransactionEntity(
                id = id.value,
                name = name,
                processingDate = processingDate.toString(),
                money = moneyEntityMapper.mapToEntity(money),
                transferDirection = transferDirection.name,
                category = category.name,
                status = status.name,
                statusCode = statusCode.name,
                attachments = attachments,
                cardDescription = cardDescription,
                noteToSelf = noteToSelf
            )
        }
    }

}