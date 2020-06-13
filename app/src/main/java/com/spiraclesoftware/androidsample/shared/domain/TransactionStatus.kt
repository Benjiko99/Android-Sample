package com.spiraclesoftware.androidsample.shared.domain

import com.spiraclesoftware.androidsample.R

enum class TransactionStatus {
    COMPLETED, DECLINED;

    val stringRes: Int
        get() = when (this) {
            COMPLETED -> R.string.transaction_status__completed
            DECLINED -> R.string.transaction_status__declined
        }

    val drawableRes: Int?
        get() = when (this) {
            DECLINED -> R.drawable.ic_status_declined
            else -> null
        }

    val colorRes: Int?
        get() = when (this) {
            DECLINED -> R.string.transaction_status__declined
            else -> null
        }
}