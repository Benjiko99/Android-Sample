package com.spiraclesoftware.androidsample.ui.transactiondetail.cards

import com.spiraclesoftware.androidsample.domain.model.Transaction

class CardsPresenter {

    /**
     * Generates [Card]s from a given [Transaction].
     *
     * These describe details about the [Transaction] that can be shown to the user.
     */
    fun getCards(transaction: Transaction) = cards {
        val isSuccessful = transaction.isSuccessful()
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
