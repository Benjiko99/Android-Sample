package com.spiraclesoftware.androidsample.ui.transactionlist

import com.mikepenz.fastadapter.GenericItem
import com.spiraclesoftware.androidsample.domain.model.TransferDirectionFilter

sealed class TransactionListViewState

object Loading : TransactionListViewState()

data class ListReady(
    val listItems: List<GenericItem>,
    val directionFilter: TransferDirectionFilter,
    val emptyState: EmptyState? = null,
) : TransactionListViewState()

object Error : TransactionListViewState()

data class EmptyState(
    val image: Int? = null,
    val caption: Int,
    val message: Int,
)