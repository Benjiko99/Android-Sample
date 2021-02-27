package com.spiraclesoftware.androidsample.feature.transaction_detail.cards.item.model

import com.spiraclesoftware.androidsample.domain.entity.TransactionStatus
import com.spiraclesoftware.androidsample.domain.entity.TransactionStatusCode
import com.spiraclesoftware.androidsample.framework.Model

data class StatusCardModel(
    val status: TransactionStatus,
    val statusCode: TransactionStatusCode
) : Model
