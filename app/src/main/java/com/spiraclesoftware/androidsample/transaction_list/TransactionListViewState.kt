package com.spiraclesoftware.androidsample.transaction_list

import com.mikepenz.fastadapter.GenericItem
import com.spiraclesoftware.androidsample.shared.TransferDirectionFilter

sealed class TransactionListViewState

object Loading : TransactionListViewState()

data class Error(val message: String?) : TransactionListViewState()

data class Content(
    val listItems: List<GenericItem>,
    val directionFilter: TransferDirectionFilter,
    val emptyState: EmptyState? = null,
) : TransactionListViewState()

data class EmptyState(
    val image: Int? = null,
    val caption: Int,
    val message: Int,
)