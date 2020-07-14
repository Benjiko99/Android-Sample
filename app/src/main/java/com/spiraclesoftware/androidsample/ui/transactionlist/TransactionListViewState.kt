package com.spiraclesoftware.androidsample.ui.transactionlist

import com.mikepenz.fastadapter.GenericItem
import com.spiraclesoftware.androidsample.domain.model.TransactionListFilter

sealed class TransactionListViewState

object Loading : TransactionListViewState()

data class ListReady(
    val listItems: List<GenericItem>,
    val listFilter: TransactionListFilter
) : TransactionListViewState()

object NetworkError : TransactionListViewState()
