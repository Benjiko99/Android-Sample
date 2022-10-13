package com.spiraclesoftware.androidsample.common.formatter

import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.domain.entity.TransactionStatus

class TransactionStatusFormatter {

    fun stringRes(status: TransactionStatus) = when (status) {
        TransactionStatus.COMPLETED -> R.string.transaction_status__completed
        TransactionStatus.DECLINED -> R.string.transaction_status__declined
    }

}