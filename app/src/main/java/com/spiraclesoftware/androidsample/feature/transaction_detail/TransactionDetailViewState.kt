package com.spiraclesoftware.androidsample.feature.transaction_detail

import com.mikepenz.fastadapter.GenericItem

sealed class TransactionDetailViewState {

    object Initial : TransactionDetailViewState()

    object Error : TransactionDetailViewState()

    data class Content(
        val detailModel: DetailModel,
        val cardItems: List<GenericItem>
    ) : TransactionDetailViewState()

}
