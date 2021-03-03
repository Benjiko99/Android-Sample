package com.spiraclesoftware.androidsample.formatter

import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.domain.entity.*
import com.spiraclesoftware.androidsample.feature.transaction_list.EmptyState
import com.spiraclesoftware.androidsample.feature.transaction_list.HeaderModel
import com.spiraclesoftware.androidsample.feature.transaction_list.TransactionListFormatter
import com.spiraclesoftware.androidsample.feature.transaction_list.TransactionModel
import org.junit.Test
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.*

class TransactionListFormatterTest : FormatterTest() {

    private val formatter = TransactionListFormatter()

    @Test
    fun formatHeaderModel() {
        val day = ZonedDateTime.parse("1970-01-01T00:00:00+00:00")
        val contribution = Money(BigDecimal("100"), Currency.getInstance("EUR"))

        val expected = HeaderModel(
            date = "01 January 1970",
            contribution = "€100"
        )
        val actual = formatter.headerModel(day, contribution)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun formatTransactionModel_successful() {
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
            statusCodeRes = null,
            contributesToAccountBalance = true,
            iconRes = R.drawable.ic_category_transfers,
            iconTintRes = R.color.transaction_category__transfers
        )
        val actual = formatter.transactionModel(transaction)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun formatTransactionModel_declined() {
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
            statusCodeRes = R.string.transaction_status_code__spending_limit_exceeded,
            contributesToAccountBalance = false,
            iconRes = R.drawable.ic_status_declined,
            iconTintRes = R.color.transaction_status__declined
        )
        val actual = formatter.transactionModel(transaction)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun formatEmptyState_whenListIsEmptyWithoutFilter() {
        val listIsEmpty = true
        val filterIsActive = false

        val expected = EmptyState(
            caption = R.string.empty_state__no_transactions__caption,
            message = R.string.empty_state__no_transactions__message
        )
        val actual = formatter.emptyState(listIsEmpty, filterIsActive)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun formatEmptyState_whenListIsEmptyAndFiltered() {
        val listIsEmpty = true
        val filterIsActive = true

        val expected = EmptyState(
            image = R.drawable.ic_empty_search_results,
            caption = R.string.empty_state__no_results__caption,
            message = R.string.empty_state__no_results__message
        )
        val actual = formatter.emptyState(listIsEmpty, filterIsActive)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun formatEmptyState_whenListIsNotEmpty_returnNothing() {
        val listIsEmpty = false
        val filterIsActive = false

        val expected = null
        val actual = formatter.emptyState(listIsEmpty, filterIsActive)

        assertThat(actual).isEqualTo(expected)
    }

}