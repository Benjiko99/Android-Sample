package com.spiraclesoftware.androidsample.feature.transaction_detail

import com.spiraclesoftware.androidsample.framework.Model

sealed class TransactionDetailViewState {

    object Initial : TransactionDetailViewState()

    data class Error(val message: String?) : TransactionDetailViewState()

    data class Content(
        val detailModel: DetailModel,
        val cardModels: List<Model>,
        val actionChips: List<ActionChip>,
    ) : TransactionDetailViewState()

}

data class ActionChip(
    val id: Int,
    val isVisible: Boolean,
)
