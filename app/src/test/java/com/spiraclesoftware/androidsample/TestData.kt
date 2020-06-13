package com.spiraclesoftware.androidsample

import com.spiraclesoftware.androidsample.shared.domain.*
import org.threeten.bp.ZonedDateTime
import java.math.BigDecimal
import java.util.*

object TestData {

    val transactions: List<Transaction> = arrayListOf(
        Transaction(
            TransactionId(1),
            "Paypal *Steam",
            ZonedDateTime.parse("2019-05-15T22:22:00+00:00"),
            Money(BigDecimal("49.99"), Currency.getInstance("EUR")),
            TransferDirection.OUTGOING,
            TransactionCategory.ENTERTAINMENT,
            TransactionStatus.COMPLETED,
            TransactionStatusCode.SUCCESSFUL,
            "VISA **9400",
            "Half-Life: Alyx"
        ),
        Transaction(
            TransactionId(2),
            "Salary",
            ZonedDateTime.parse("2019-05-14T09:00:00+00:00"),
            Money(BigDecimal("1000.00"), Currency.getInstance("EUR")),
            TransferDirection.INCOMING,
            TransactionCategory.TRANSFERS,
            TransactionStatus.COMPLETED,
            TransactionStatusCode.SUCCESSFUL
        ),
        Transaction(
            TransactionId(3),
            "Groceries",
            ZonedDateTime.parse("2019-05-14T09:00:00+00:00"),
            Money(BigDecimal("14.99"), Currency.getInstance("USD")),
            TransferDirection.OUTGOING,
            TransactionCategory.GROCERIES,
            TransactionStatus.DECLINED,
            TransactionStatusCode.SPENDING_LIMIT_EXCEEDED
        )
    )

    val transactionsIncoming: List<Transaction>
        get() = transactions.filter {
            it.transferDirection == TransferDirection.INCOMING
        }

    val transactionsOutgoing: List<Transaction>
        get() = transactions.filter {
            it.transferDirection == TransferDirection.OUTGOING
        }
}