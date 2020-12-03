package com.spiraclesoftware.androidsample.domain.model

import androidx.annotation.StringRes
import com.spiraclesoftware.androidsample.R

/**
 * Defines possible ways to filter by [TransferDirection].
 *
 * @param direction maps the filter enum to the real [TransferDirection] enums
 * @param stringRes UI friendly string
 */
enum class TransferDirectionFilter(
    private val direction: TransferDirection?,
    @StringRes val stringRes: Int
) {
    ALL(null, R.string.transfer_direction_filter__all),
    INCOMING_ONLY(TransferDirection.INCOMING, R.string.transfer_direction_filter__incoming),
    OUTGOING_ONLY(TransferDirection.OUTGOING, R.string.transfer_direction_filter__outgoing);

    /**
     * @return whether this [TransferDirectionFilter] corresponds to the passed [TransferDirection]
     */
    fun mapsTo(direction: TransferDirection) = this.direction == direction
}