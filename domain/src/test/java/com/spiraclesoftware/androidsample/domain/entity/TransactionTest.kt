package com.spiraclesoftware.androidsample.domain.entity

import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.domain.transaction
import org.junit.Test

class TransactionTest {

    @Test
    fun `Successful transactions contribute to account balance`() {
        val transaction = transaction(
            status = TransactionStatus.COMPLETED,
            statusCode = TransactionStatusCode.SUCCESSFUL
        )

        assertThat(transaction.contributesToAccountBalance()).isTrue()
    }

    @Test
    fun `Declined transactions do not contribute to account balance`() {
        val transaction = transaction(
            status = TransactionStatus.DECLINED,
            statusCode = TransactionStatusCode.SPENDING_LIMIT_EXCEEDED
        )

        assertThat(transaction.contributesToAccountBalance()).isFalse()
    }

}