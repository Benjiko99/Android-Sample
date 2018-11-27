package com.spiraclesoftware.airbankinterview.shared.domain

import androidx.annotation.ColorRes
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

    @ColorRes
    fun getColorRes(): Int = when (this) {
        INCOMING -> R.color.transaction_incoming
        OUTGOING -> R.color.transaction_outgoing
    }
}