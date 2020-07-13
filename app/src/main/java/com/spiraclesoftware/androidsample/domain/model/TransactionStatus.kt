package com.spiraclesoftware.androidsample.domain.model

import com.spiraclesoftware.androidsample.R

enum class TransactionStatus {
    COMPLETED, DECLINED;

    val stringRes: Int
        get() = when (this) {
            COMPLETED -> R.string.transaction_status__completed
            DECLINED -> R.string.transaction_status__declined
        }

}