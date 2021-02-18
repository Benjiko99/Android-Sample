package com.spiraclesoftware.androidsample.domain.entity

import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.domain.epochDateTime
import com.spiraclesoftware.androidsample.domain.money
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

        assertThat(transaction.contributesToAccountBalance()).isTrue()
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

        assertThat(transaction.contributesToAccountBalance()).isFalse()
    }

}