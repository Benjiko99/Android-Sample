package com.spiraclesoftware.androidsample.ui.transactiondetail

import co.zsmb.rainbowcake.withIOContext
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.domain.model.TransactionId
import com.spiraclesoftware.androidsample.ui.transactiondetail.cards.Card
import com.spiraclesoftware.androidsample.ui.transactiondetail.cards.CardsGenerator

class TransactionDetailPresenter(
    private val transactionsInteractor: TransactionsInteractor,
    private val cardsGenerator: CardsGenerator
) {

    suspend fun getTransactionById(id: TransactionId): Transaction? = withIOContext {
        transactionsInteractor.getTransactionById(id)
    }

    suspend fun getCardItems(
        transaction: Transaction,
        clickAction: (Int) -> Unit
    ): List<CardItem> = withIOContext {
        cardsGenerator.makeCardsFor(transaction)
            .toListItems(transaction, clickAction)
    }

    private fun List<Card>.toListItems(
        transaction: Transaction,
        clickAction: (Int) -> Unit
    ) = map { card ->
        CardItem(card, transaction).apply {
            withActionClickHandler(clickAction)
        }
    }

}
