package com.spiraclesoftware.androidsample.domain.model

import com.spiraclesoftware.androidsample.R

enum class TransactionStatusCode {
    SUCCESSFUL, SPENDING_LIMIT_EXCEEDED;

    val stringRes: Int?
        get() = when (this) {
            SPENDING_LIMIT_EXCEEDED -> R.string.transaction_status__code__spending_limit_exceeded
            else -> null
        }
}