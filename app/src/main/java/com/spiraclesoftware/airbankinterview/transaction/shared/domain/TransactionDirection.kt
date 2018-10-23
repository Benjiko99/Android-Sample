package com.spiraclesoftware.airbankinterview.transaction.shared.domain

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.spiraclesoftware.airbankinterview.R

enum class TransactionDirection {
    INCOMING, OUTGOING;

    @StringRes
    fun getStringRes(): Int = when (this) {
        INCOMING -> R.string.transaction_direction__incoming
        OUTGOING -> R.string.transaction_direction__outgoing
    }

    @DrawableRes
    fun getDrawableRes(): Int = when (this) {
        INCOMING -> R.drawable.ic_transaction_incoming
        OUTGOING -> R.drawable.ic_transaction_outgoing
    }
}