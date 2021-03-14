package com.spiraclesoftware.androidsample.feature.transaction_detail

import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.domain.entity.*
import com.spiraclesoftware.androidsample.format.FormatterTest
import org.junit.Test
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.*

class TransactionDetailFormatterTest : FormatterTest() {

    @Test
    fun formatDetailModel() {
        val transaction = Transaction(
            TransactionId("1"),
            "Name",
            ZonedDateTime.parse("1970-01-01T00:00:00+00:00")!!,
            Money(BigDecimal("49.99"), Currency.getInstance("EUR")),
            TransferDirection.OUTGOING,
            TransactionCategory.ENTERTAINMENT,
            TransactionStatus.COMPLETED,
            TransactionStatusCode.SUCCESSFUL,
        )

        val expected = DetailModel(
            TransactionId("1"),
            name = "Name",
            formattedMoney = "- €49.99",
            processingDate = "00:00, Thu, 01 January",
            iconRes = R.drawable.ic_category_entertainment,
            iconTintRes = R.color.transaction_category__entertainment,
            contributesToBalance = true,
            isSuccessful = true,
            cardModels = emptyList()
        )

        val formatter = TransactionDetailFormatter()
        val actual = formatter.detailModel(transaction, emptyList())

        assertThat(actual).isEqualTo(expected)
    }

}