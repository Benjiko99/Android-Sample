package com.spiraclesoftware.airbankinterview

import com.spiraclesoftware.airbankinterview.transaction.list.domain.Transaction
import com.spiraclesoftware.airbankinterview.transaction.shared.domain.TransactionDirection

object TestData {

    val transactions: List<Transaction> = arrayListOf(
        Transaction(1, 1000, TransactionDirection.INCOMING),
        Transaction(2, 2000, TransactionDirection.OUTGOING)
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