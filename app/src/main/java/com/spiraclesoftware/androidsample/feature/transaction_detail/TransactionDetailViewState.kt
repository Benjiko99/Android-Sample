package com.spiraclesoftware.androidsample.feature.transaction_detail

import com.mikepenz.fastadapter.GenericItem
import com.spiraclesoftware.androidsample.domain.entity.TransactionCategory
import java.time.ZonedDateTime

sealed class TransactionDetailViewState {

    object Initial : TransactionDetailViewState()

    object Error : TransactionDetailViewState()

    data class Content(
        val name: String,
        val processingDate: ZonedDateTime,
        val formattedMoney: String,
        val contributesToBalance: Boolean,
        val isSuccessful: Boolean,
        val category: TransactionCategory,
        val cardItems: List<GenericItem>
    ) : TransactionDetailViewState()

}
