package com.spiraclesoftware.androidsample.shared.domain

import androidx.annotation.StringRes
import com.spiraclesoftware.androidsample.R

/**
 * Defines possible ways to filter by [TransactionDirection].
 *
 * @param direction maps the filter enum to the real [TransactionDirection] enums
 * @param stringRes UI friendly string
 */
enum class TransactionDirectionFilter(private val direction: TransactionDirection?, @StringRes val stringRes: Int) {
    ALL(null, R.string.transaction__list__filter__transaction_direction__all),
    INCOMING_ONLY(TransactionDirection.INCOMING, R.string.transaction__list__filter__transaction_direction__incoming),
    OUTGOING_ONLY(TransactionDirection.OUTGOING, R.string.transaction__list__filter__transaction_direction__outgoing);

    /**
     * @return whether this [TransactionDirectionFilter] corresponds to the passed [TransactionDirection]
     */
    fun mapsTo(direction: TransactionDirection) = this.direction == direction
}