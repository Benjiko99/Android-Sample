package com.spiraclesoftware.androidsample.ui.transactiondetail.cards

import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.domain.model.TransactionStatus
import com.spiraclesoftware.androidsample.domain.model.TransactionStatusCode

/**
 * Generates [Card]s from a given [Transaction].
 *
 * These describe details about the [Transaction] that can be shown to the user.
 */
class CardsGenerator {

    fun makeCardsFor(transaction: Transaction) = cards {

        // expanded status card
        card {
            if (transaction.statusCode != TransactionStatusCode.SUCCESSFUL) {
                transactionStatus(expanded = true)
            }
        }

        // details card
        card {
            if (transaction.statusCode == TransactionStatusCode.SUCCESSFUL) {
                transactionStatus(expanded = false)
            }

            if (!transaction.cardDescription.isNullOrEmpty()) {
                cardDescription()
            }

            if (transaction.status == TransactionStatus.COMPLETED) {
                transactionStatement()
            }
        }

        // category card
        card {
            if (transaction.statusCode == TransactionStatusCode.SUCCESSFUL) {
                transactionCategory()
            }
        }

        // note card
        card {
            noteToSelf()
        }
    }

}