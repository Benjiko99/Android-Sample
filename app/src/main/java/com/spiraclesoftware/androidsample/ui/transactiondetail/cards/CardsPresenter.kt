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
        getCardsFor(transaction).toListItems(transaction, actionsHandler)
    }

    /**
     * Generates [Card]s from a given [Transaction].
     *
     * These describe details about the [Transaction] that can be shown to the user.
     */
    fun getCardsFor(transaction: Transaction): List<Card> {
        return cards {
            if (!TransactionsPolicy.isSuccessful(transaction)) {
                statusCard()
            }

            // details card
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

            // category card
            valuePairCard {
                if (TransactionsPolicy.isSuccessful(transaction)) {
                    category()
                }
            }

            // attachments card
            valuePairCard {
                if (TransactionsPolicy.isSuccessful(transaction)) {
                    attachments()
                }
            }

            noteToSelfCard()
        }
    }

    private fun List<Card>.toListItems(
        transaction: Transaction,
        actionsHandler: CardActionsHandler
    ): List<CardItem<*>> = this.map { card ->
        when (card) {
            is ValuePairCard -> card.toListItem(transaction, actionsHandler)
            is StatusCard -> card.toListItem(transaction)
            is NoteCard -> card.toListItem(transaction, actionsHandler)
        }
    }

}
