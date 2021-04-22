package com.spiraclesoftware.androidsample.feature.transaction_detail.cards

import android.net.Uri
import com.spiraclesoftware.androidsample.domain.entity.Transaction
import com.spiraclesoftware.androidsample.framework.Model

class CardsPresenter(
    private val cardsFormatter: CardsFormatter,
) {

    fun getCardModels(
        transaction: Transaction,
        attachmentUploads: List<Uri>
    ): List<Model> {
        val cards = getCards(transaction, attachmentUploads)
        return cardsFormatter.cardModels(cards)
    }

    /**
     * Generates [Card]s from a given [Transaction].
     *
     * These describe details about the [Transaction] that can be shown to the user.
     */
    fun getCards(
        transaction: Transaction,
        attachmentUploads: List<Uri>
    ) = with(transaction) {
        cards {
            val isSuccessful = isSuccessful()
            val hasCardDescription = !cardDescription.isNullOrEmpty()

            if (!isSuccessful)
                statusCard(status, statusCode)

            valuePairCard {
                if (isSuccessful)
                    status(status)

                if (hasCardDescription)
                    cardDescription(cardDescription)

                if (isSuccessful)
                    downloadStatement()
            }

            if (isSuccessful) {
                categoryCard(category)
                attachmentsCard(attachments, attachmentUploads)
            }

            noteCard(noteToSelf)
        }
    }

}
