package com.spiraclesoftware.androidsample

import com.spiraclesoftware.androidsample.shared.domain.*

object TestData {

    val transactions: List<Transaction> = arrayListOf(
        Transaction(TransactionId(1), 1000, TransactionDirection.INCOMING),
        Transaction(TransactionId(2), 2000, TransactionDirection.OUTGOING)
    )

    val transactionsIncoming: List<Transaction>
        get() = transactions.filter {
            it.direction == TransactionDirection.INCOMING
        }

    val transactionsOutgoing: List<Transaction>
        get() = transactions.filter {
            it.direction == TransactionDirection.OUTGOING
        }
}