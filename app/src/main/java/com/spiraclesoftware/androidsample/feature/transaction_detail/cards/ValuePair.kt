package com.spiraclesoftware.androidsample.feature.transaction_detail.cards

import com.spiraclesoftware.androidsample.domain.entity.TransactionStatus

sealed class ValuePair {

    data class Status(
        val status: TransactionStatus
    ) : ValuePair()

    data class CardDescription(
        val cardDescription: String?
    ) : ValuePair()

    object DownloadStatement : ValuePair()

}