package com.spiraclesoftware.androidsample.common.formatter

import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.domain.entity.TransferDirection
import com.spiraclesoftware.androidsample.domain.entity.TransferDirectionFilter

class TransferDirectionFilterFormatter {

    fun stringRes(filter: TransferDirectionFilter) = when (filter.direction) {
        null -> R.string.transfer_direction_filter__all
        TransferDirection.INCOMING -> R.string.transfer_direction_filter__incoming
        TransferDirection.OUTGOING -> R.string.transfer_direction_filter__outgoing
    }

    fun listStringRes(): List<Int> {
        return TransferDirectionFilter.values().map { stringRes(it) }
    }

}