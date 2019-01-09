package com.spiraclesoftware.androidsample.shared.domain

import com.spiraclesoftware.core.data.Identifiable

data class Transaction(
    val id: TransactionId,
    val amountInAccountCurrency: Int,
    val direction: TransactionDirection
) : Identifiable<TransactionId> {

    override fun getUniqueId() = id
}
