package com.spiraclesoftware.androidsample.domain

import com.spiraclesoftware.androidsample.domain.entity.*
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.*

val epochDateTime = ZonedDateTime.parse("1970-01-01T00:00:00+00:00")!!

/** Shorthand constructor for Money */
fun money(amount: String, currencyCode: String): Money {
    return Money(BigDecimal(amount), Currency.getInstance(currencyCode))
}

fun transaction(
    id: TransactionId = TransactionId("1"),
    name: String = "Transaction #${id.value}",
    processingDate: ZonedDateTime = epochDateTime,
    money: Money = money("100", "EUR"),
    transferDirection: TransferDirection = TransferDirection.OUTGOING,
    category: TransactionCategory = TransactionCategory.ENTERTAINMENT,
    status: TransactionStatus = TransactionStatus.COMPLETED,
    statusCode: TransactionStatusCode = TransactionStatusCode.SUCCESSFUL,
    attachments: List<String> = emptyList(),
    cardDescription: String? = null,
    noteToSelf: String? = null
) = Transaction(
    id = id,
    name = name,
    processingDate = processingDate,
    money = money,
    transferDirection = transferDirection,
    category = category,
    status = status,
    statusCode = statusCode,
    attachments = attachments,
    cardDescription = cardDescription,
    noteToSelf = noteToSelf,
)
