package com.spiraclesoftware.androidsample.ui.transactiondetail

sealed class TransactionDetailViewState

object Initial : TransactionDetailViewState()

object Loading : TransactionDetailViewState()

data class TransactionDetailReady(val data: String = "") : TransactionDetailViewState()
