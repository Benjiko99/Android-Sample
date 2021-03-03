package com.spiraclesoftware.androidsample.feature.transaction_detail.cards

import android.net.Uri
import com.spiraclesoftware.androidsample.domain.entity.TransactionCategory
import com.spiraclesoftware.androidsample.domain.entity.TransactionStatus
import com.spiraclesoftware.androidsample.domain.entity.TransactionStatusCode

@DslMarker
annotation class CardsBuilderDSL

@CardsBuilderDSL
class ValuePairCardBuilder {

    private val valuePairs = mutableListOf<ValuePair>()

    fun status(status: TransactionStatus) {
        valuePairs += ValuePair.Status(status)
    }

    fun cardDescription(cardDescription: String?) {
        valuePairs += ValuePair.CardDescription(cardDescription)
    }

    fun downloadStatement() {
        valuePairs += ValuePair.DownloadStatement
    }

    fun build() = ValuePairCard(valuePairs)

    fun isValid() = valuePairs.isNotEmpty()

}

@CardsBuilderDSL
class CardsBuilder {

    private val cards = mutableListOf<Card>()

    fun valuePairCard(setup: ValuePairCardBuilder.() -> Unit = {}) {
        val cardBuilder = ValuePairCardBuilder()
        cardBuilder.setup()

        if (cardBuilder.isValid()) {
            cards += cardBuilder.build()
        }
    }

    fun statusCard(
        status: TransactionStatus,
        statusCode: TransactionStatusCode
    ) {
        cards += StatusCard(status, statusCode)
    }

    fun categoryCard(category: TransactionCategory) {
        cards += CategoryCard(category)
    }

    fun attachmentsCard(
        attachments: List<String>,
        uploads: List<Uri>
    ) {
        cards += AttachmentsCard(attachments, uploads)
    }

    fun noteCard(note: String?) {
        cards += NoteCard(note)
    }

    fun build() = cards

    /**
     * This method shadows the [cards] method when inside the scope
     * of a [CardsBuilder], so that cards can't be nested.
     */
    @Suppress("UNUSED_PARAMETER")
    @Deprecated(
        level = DeprecationLevel.ERROR,
        message = "Cards can't be nested."
    )
    fun cards(param: () -> Unit = {}) {
    }

    operator fun Card.unaryPlus() {
        cards += this
    }

}

fun cards(setup: CardsBuilder.() -> Unit): List<Card> {
    val cardsBuilder = CardsBuilder()
    cardsBuilder.setup()
    return cardsBuilder.build()
}