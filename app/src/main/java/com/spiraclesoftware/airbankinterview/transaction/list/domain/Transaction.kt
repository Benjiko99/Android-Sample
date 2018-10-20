package com.spiraclesoftware.airbankinterview.transaction.list.domain

import com.spiraclesoftware.airbankinterview.transaction.shared.domain.TransactionDirection

typealias TransactionId = Int

data class Transaction(
    val id: TransactionId,
    val amountInAccountCurrency: Int,
    val direction: TransactionDirection
)