package com.spiraclesoftware.androidsample.feature.transaction_list

import com.spiraclesoftware.androidsample.framework.Model

sealed class TransactionListViewState {

    object Loading : TransactionListViewState()

    data class Error(val message: String?) : TransactionListViewState()

    data class Content(
        val listModels: List<Model>,
        val filterModel: FilterModel,
        val emptyState: EmptyState? = null,
    ) : TransactionListViewState()

}

data class EmptyState(
    val image: Int? = null,
    val caption: Int,
    val message: Int,
)
