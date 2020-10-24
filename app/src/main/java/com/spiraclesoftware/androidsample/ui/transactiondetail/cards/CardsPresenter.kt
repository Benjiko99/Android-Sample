package com.spiraclesoftware.androidsample.ui.transactiondetail.cards

import co.zsmb.rainbowcake.withIOContext
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.domain.policy.TransactionsPolicy
import com.spiraclesoftware.androidsample.ui.transactiondetail.cards.items.CardItem

class CardsPresenter {

    suspend fun getCardItems(
        transaction: Transaction,
        actionsHandler: CardActionsHandler
    ): List<CardItem<*>> = withIOContext {
        getCardsFor(transaction).map {
            it.toListItem(transaction, actionsHandler)
        }
    }

    /**
     * Generates [Card]s from a given [Transaction].
     *
     * These describe details about the [Transaction] that can be shown to the user.
     */
    fun getCardsFor(transaction: Transaction) = cards {
        val isSuccessful = TransactionsPolicy.isSuccessful(transaction)
        val hasCardDescription = !transaction.cardDescription.isNullOrEmpty()

        if (!isSuccessful)
            statusCard()

        valuePairCard {
            if (isSuccessful)
                status()

            if (hasCardDescription)
                cardDescription()

            if (isSuccessful)
                downloadStatement()
        }

        if (isSuccessful) {
            categoryCard()
            attachmentsCard()
        }

        noteCard()
    }

}
