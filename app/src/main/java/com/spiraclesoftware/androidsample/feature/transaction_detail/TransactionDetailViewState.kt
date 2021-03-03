package com.spiraclesoftware.androidsample.feature.transaction_detail

sealed class TransactionDetailViewState {

    object Initial : TransactionDetailViewState()

    data class Error(val message: String?) : TransactionDetailViewState()

    data class Content(
        val detailModel: DetailModel
    ) : TransactionDetailViewState()

}
