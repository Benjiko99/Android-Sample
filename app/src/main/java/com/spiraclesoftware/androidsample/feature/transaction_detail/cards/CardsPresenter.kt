package com.spiraclesoftware.androidsample.feature.transaction_detail.cards

import android.net.Uri
import com.spiraclesoftware.androidsample.domain.entity.Transaction
import com.spiraclesoftware.androidsample.framework.Model

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

    fun cardModels(
        transaction: Transaction,
        uploads: List<Uri>
    ): List<Model> {
        return getCards(transaction).map { card ->
            when (card) {
                is ValuePairCard ->
                    card.toModel(transaction)
                is StatusCard ->
                    card.toModel(transaction.status, transaction.statusCode)
                is CategoryCard ->
                    card.toModel(transaction.category)
                is AttachmentsCard ->
                    card.toModel(transaction.attachments, uploads)
                is NoteCard ->
                    card.toModel(transaction.noteToSelf)
            }
        }
    }

}
