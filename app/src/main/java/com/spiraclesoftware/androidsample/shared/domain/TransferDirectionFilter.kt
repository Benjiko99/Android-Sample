package com.spiraclesoftware.androidsample.shared.domain

import androidx.annotation.StringRes
import com.spiraclesoftware.androidsample.R

/**
 * Defines possible ways to filter by [TransferDirection].
 *
 * @param direction maps the filter enum to the real [TransferDirection] enums
 * @param stringRes UI friendly string
 */
enum class TransferDirectionFilter(private val direction: TransferDirection?, @StringRes val stringRes: Int) {
    ALL(null, R.string.transaction__list__filter__transfer_direction__all),
    INCOMING_ONLY(TransferDirection.INCOMING, R.string.transaction__list__filter__transfer_direction__incoming),
    OUTGOING_ONLY(TransferDirection.OUTGOING, R.string.transaction__list__filter__transfer_direction__outgoing);

    /**
     * @return whether this [TransferDirectionFilter] corresponds to the passed [TransferDirection]
     */
    fun mapsTo(direction: TransferDirection) = this.direction == direction
}