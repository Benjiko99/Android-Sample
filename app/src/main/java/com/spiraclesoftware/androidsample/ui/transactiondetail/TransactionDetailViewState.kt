package com.spiraclesoftware.androidsample.ui.transactiondetail

import com.spiraclesoftware.androidsample.domain.model.Transaction

sealed class TransactionDetailViewState

object Loading : TransactionDetailViewState()

data class DetailReady(
    val transaction: Transaction,
    val cardItems: List<CardItem>
) : TransactionDetailViewState()
