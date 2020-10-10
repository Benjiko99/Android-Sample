package com.spiraclesoftware.androidsample.ui.transactiondetail.cards

import co.zsmb.rainbowcake.withIOContext
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.domain.policy.TransactionsPolicy

class CardsPresenter {

    suspend fun getCardItems(
        transaction: Transaction,
        clickAction: (Int) -> Unit
    ): List<CardItem> = withIOContext {
        getCardsFor(transaction).toListItems(transaction, clickAction)
    }

    private fun List<Card>.toListItems(
        transaction: Transaction,
        clickAction: (Int) -> Unit
    ) = map { card ->
        CardItem(card, transaction).apply {
            withActionClickHandler(clickAction)
        }
    }

    /**
     * Generates [Card]s from a given [Transaction].
     *
     * These describe details about the [Transaction] that can be shown to the user.
     */
    fun getCardsFor(transaction: Transaction): List<Card> {
        return cards {
            // expanded status card
            card {
                if (!TransactionsPolicy.isSuccessful(transaction)) {
                    transactionStatus(expanded = true)
                }
            }

            // details card
            card {
                if (TransactionsPolicy.isSuccessful(transaction)) {
                    transactionStatus(expanded = false)
                }

                if (!transaction.cardDescription.isNullOrEmpty()) {
                    cardDescription()
                }

                if (TransactionsPolicy.isSuccessful(transaction)) {
                    transactionStatement()
                }
            }

            // category card
            card {
                if (TransactionsPolicy.isSuccessful(transaction)) {
                    transactionCategory()
                }
            }

            // attachments card
            card {
                if (TransactionsPolicy.isSuccessful(transaction)) {
                    attachments()
                }
            }

            // note card
            card {
                noteToSelf()
            }
        }
    }

}