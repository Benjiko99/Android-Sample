package com.spiraclesoftware.androidsample.ui.transactiondetail

import com.spiraclesoftware.androidsample.ui.transactiondetail.cards.Card
import com.spiraclesoftware.androidsample.shared.domain.Transaction

sealed class TransactionDetailViewState

object Loading : TransactionDetailViewState()

data class DetailReady(
    val transaction: Transaction,
    val cards: List<Card>
) : TransactionDetailViewState()
