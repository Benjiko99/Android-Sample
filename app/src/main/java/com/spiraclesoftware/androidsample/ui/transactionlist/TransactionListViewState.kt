package com.spiraclesoftware.androidsample.ui.transactionlist

import com.mikepenz.fastadapter.GenericItem

sealed class TransactionListViewState

object Loading : TransactionListViewState()

data class ListReady(val listItems: List<GenericItem>) : TransactionListViewState()

object NetworkError : TransactionListViewState()
