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
        if (!TransactionsPolicy.isSuccessful(transaction)) {
            statusCard()
        }

        valuePairCard {
            if (TransactionsPolicy.isSuccessful(transaction)) {
                status()
            }

            if (!transaction.cardDescription.isNullOrEmpty()) {
                paymentCard()
            }

            if (TransactionsPolicy.isSuccessful(transaction)) {
                statement()
            }
        }

        if (TransactionsPolicy.isSuccessful(transaction)) {
            categoryCard()
            attachmentsCard()
        }

        noteCard()
    }

}
