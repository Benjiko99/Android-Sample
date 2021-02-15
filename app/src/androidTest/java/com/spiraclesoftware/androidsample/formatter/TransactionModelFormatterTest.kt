package com.spiraclesoftware.androidsample.formatter

import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.domain.entity.*
import com.spiraclesoftware.androidsample.feature.transaction_list.TransactionModel
import com.spiraclesoftware.androidsample.feature.transaction_list.TransactionModelFormatter
import org.junit.Test
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.*

class TransactionModelFormatterTest : FormatterTest() {

    @Test
    fun format_Successful() {
        val transaction = Transaction(
            TransactionId("1"),
            "Salary",
            ZonedDateTime.parse("1970-01-01T00:00:00+00:00"),
            Money(BigDecimal("100"), Currency.getInstance("EUR")),
            TransferDirection.INCOMING,
            TransactionCategory.TRANSFERS,
            TransactionStatus.COMPLETED,
            TransactionStatusCode.SUCCESSFUL
        )

        val expected = TransactionModel(
            id = TransactionId("1"),
            amount = "+ €100",
            name = "Salary",
            processingDate = "00:00, Thu, 01 January",
            status = null,
            contributesToAccountBalance = true,
            iconRes = R.drawable.ic_category_transfers,
            iconTintRes = R.color.transaction_category__transfers
        )
        val actual = TransactionModelFormatter(context).format(transaction)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun format_Declined() {
        val transaction = Transaction(
            TransactionId("1"),
            "Payment",
            ZonedDateTime.parse("1970-01-01T00:00:00+00:00"),
            Money(BigDecimal("100"), Currency.getInstance("EUR")),
            TransferDirection.OUTGOING,
            TransactionCategory.TRANSFERS,
            TransactionStatus.DECLINED,
            TransactionStatusCode.SPENDING_LIMIT_EXCEEDED
        )

        val expected = TransactionModel(
            id = TransactionId("1"),
            amount = "€100",
            name = "Payment",
            processingDate = "00:00, Thu, 01 January",
            status = "Monthly spending limit exceeded",
            contributesToAccountBalance = false,
            iconRes = R.drawable.ic_status_declined,
            iconTintRes = R.color.transaction_status__declined
        )
        val actual = TransactionModelFormatter(context).format(transaction)

        assertThat(actual).isEqualTo(expected)
    }

}