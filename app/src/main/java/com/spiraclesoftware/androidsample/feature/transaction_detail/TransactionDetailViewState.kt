package com.spiraclesoftware.androidsample.feature.transaction_detail

sealed class TransactionDetailViewState {

    object Initial : TransactionDetailViewState()

    object Error : TransactionDetailViewState()

    data class Content(
        val detailModel: DetailModel
    ) : TransactionDetailViewState()

}
