package com.spiraclesoftware.androidsample.shared.domain

import androidx.annotation.StringRes
import com.spiraclesoftware.androidsample.R

enum class TransactionStatus {
    COMPLETED, DECLINED;

    @StringRes
    fun getStringRes(): Int = when (this) {
        COMPLETED -> R.string.transaction_status__completed
        DECLINED -> R.string.transaction_status__declined
    }
}