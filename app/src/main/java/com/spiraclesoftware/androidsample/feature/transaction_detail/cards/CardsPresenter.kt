package com.spiraclesoftware.androidsample.feature.transaction_detail.cards

import android.net.Uri
import com.spiraclesoftware.androidsample.domain.entity.Transaction

class CardsPresenter {

    /**
     * Generates [Card]s from a given [Transaction].
     *
     * These describe details about the [Transaction] that can be shown to the user.
     */
    fun getCards(
        transaction: Transaction,
        uploads: List<Uri>
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
                attachmentsCard(attachments, uploads)
            }

            noteCard(noteToSelf)
        }
    }

}
