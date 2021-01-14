package com.spiraclesoftware.androidsample.transaction_detail.cards

@DslMarker
annotation class CardsBuilderDSL

@CardsBuilderDSL
class ValuePairCardBuilder {

    private val valuePairs = mutableListOf<ValuePair>()

    fun status() {
        valuePairs += ValuePairs.Status()
    }

    fun cardDescription() {
        valuePairs += ValuePairs.CardDescription()
    }

    fun downloadStatement() {
        valuePairs += ValuePairs.DownloadStatement()
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

    fun statusCard() {
        cards += StatusCard
    }

    fun categoryCard() {
        cards += CategoryCard
    }

    fun attachmentsCard() {
        cards += AttachmentsCard
    }

    fun noteCard() {
        cards += NoteCard
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