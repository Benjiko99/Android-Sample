package com.spiraclesoftware.androidsample.domain.model

import com.spiraclesoftware.androidsample.domain.epochDateTime
import com.spiraclesoftware.androidsample.domain.money
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TransactionTest {

    @Test
    fun `Successful transactions contribute to account balance`() {
        val transaction = Transaction(
            TransactionId("1"),
            "",
            epochDateTime,
            money("100", "EUR"),
            TransferDirection.INCOMING,
            TransactionCategory.TRANSFERS,
            TransactionStatus.COMPLETED,
            TransactionStatusCode.SUCCESSFUL
        )

        assertTrue(transaction.contributesToAccountBalance())
    }

    @Test
    fun `Declined transactions do not contribute to account balance`() {
        val transaction = Transaction(
            TransactionId("1"),
            "",
            epochDateTime,
            money("100", "EUR"),
            TransferDirection.INCOMING,
            TransactionCategory.TRANSFERS,
            TransactionStatus.DECLINED,
            TransactionStatusCode.SPENDING_LIMIT_EXCEEDED
        )

        assertFalse(transaction.contributesToAccountBalance())
    }

}