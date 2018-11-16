package com.spiraclesoftware.airbankinterview.shared.domain

typealias TransactionId = Int

data class Transaction(
    val id: TransactionId,
    val amountInAccountCurrency: Int,
    val direction: TransactionDirection
)