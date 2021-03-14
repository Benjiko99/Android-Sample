package com.spiraclesoftware.androidsample.format

import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.domain.entity.TransactionStatusCode

class TransactionStatusCodeFormatter {

    fun stringRes(statusCode: TransactionStatusCode?) = when (statusCode) {
        TransactionStatusCode.SPENDING_LIMIT_EXCEEDED -> R.string.transaction_status_code__spending_limit_exceeded
        else -> null
    }

}