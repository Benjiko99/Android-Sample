package com.spiraclesoftware.androidsample.ui.transactionlist

import com.mikepenz.fastadapter.GenericItem
import com.spiraclesoftware.androidsample.ui.shared.TransferDirectionFilter

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