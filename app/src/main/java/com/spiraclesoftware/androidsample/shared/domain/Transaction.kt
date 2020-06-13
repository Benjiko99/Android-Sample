package com.spiraclesoftware.androidsample.shared.domain

import com.spiraclesoftware.core.data.Identifiable
import org.threeten.bp.ZonedDateTime

data class Transaction(
    val id: TransactionId,
    val name: String,
    val processingDate: ZonedDateTime,
    val money: Money,
    val transferDirection: TransferDirection,
    val category: TransactionCategory,
    val status: TransactionStatus,
    val statusCode: TransactionStatusCode,
    val cardDescription: String? = null,
    val noteToSelf: String? = null
) : Identifiable<TransactionId> {

    override fun getUniqueId() = id

    val formattedMoney: String
        get() = money.format(transferDirection)
}
