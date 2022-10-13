package com.spiraclesoftware.androidsample.data_remote.mapper

import com.spiraclesoftware.androidsample.data_remote.dto.TransactionDto
import com.spiraclesoftware.androidsample.domain.entity.*

class TransactionMapper(
    private val moneyMapper: MoneyMapper
) : Mapper<TransactionDto, Transaction> {

    override fun mapToDomain(obj: TransactionDto): Transaction {
        return Transaction(
            id = TransactionId(obj.id),
            name = obj.name,
            processingDate = obj.processingDate,
            money = moneyMapper.mapToDomain(obj.money),
            transferDirection = TransferDirection.valueOf(obj.transferDirection),
            category = TransactionCategory.valueOf(obj.category),
            status = TransactionStatus.valueOf(obj.status),
            statusCode = TransactionStatusCode.valueOf(obj.statusCode),
            attachments = obj.attachments,
            cardDescription = obj.cardDescription,
            noteToSelf = obj.noteToSelf,
        )
    }

    override fun mapToRemote(obj: Transaction): TransactionDto {
        return TransactionDto(
            id = obj.id.value,
            name = obj.name,
            processingDate = obj.processingDate,
            money = moneyMapper.mapToRemote(obj.money),
            transferDirection = obj.transferDirection.name,
            category = obj.category.name,
            status = obj.status.name,
            statusCode = obj.statusCode.name,
            attachments = obj.attachments,
            cardDescription = obj.cardDescription,
            noteToSelf = obj.noteToSelf,
        )
    }

}