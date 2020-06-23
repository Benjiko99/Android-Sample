package com.spiraclesoftware.androidsample.shared.domain

import com.spiraclesoftware.androidsample.TestData
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.util.*

class TransactionTest {

    @Test
    fun `Given a successful transaction it contributes to the account balance`() {
        val transaction = Transaction(
            TransactionId(1),
            "",
            TestData.epochDateTime,
            Money("49.99", "EUR"),
            TransferDirection.INCOMING,
            TransactionCategory.TRANSFERS,
            TransactionStatus.COMPLETED,
            TransactionStatusCode.SUCCESSFUL
        )

        val contribution = transaction.getContributionToBalance(TestData.conversionRates, Currency.getInstance("EUR"))

        assertEquals(Money("49.99", "EUR"), contribution)
    }

    @Test
    fun `Given a declined transaction it does not contribute to the account balance`() {
        val transaction = Transaction(
            TransactionId(1),
            "",
            TestData.epochDateTime,
            Money("49.99", "EUR"),
            TransferDirection.OUTGOING,
            TransactionCategory.TRANSFERS,
            TransactionStatus.DECLINED,
            TransactionStatusCode.SPENDING_LIMIT_EXCEEDED
        )

        val contribution = transaction.getContributionToBalance(TestData.conversionRates, Currency.getInstance("EUR"))

        assertEquals(Money("0", "EUR"), contribution)
    }
}