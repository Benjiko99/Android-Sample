package com.spiraclesoftware.androidsample.ui.transactiondetail.cards

@DslMarker
annotation class CardsBuilderDSL

@CardsBuilderDSL
class CardBuilder {

    private val valuePairs = mutableListOf<CardValuePair>()

    fun transactionStatus(expanded: Boolean) {
        valuePairs += CardValuePairs.TransactionStatus(expanded)
    }

    fun cardDescription() {
        valuePairs += CardValuePairs.CardDescription()
    }

    fun transactionStatement() {
        valuePairs += CardValuePairs.TransactionStatement()
    }

    fun transactionCategory() {
        valuePairs += CardValuePairs.TransactionCategory()
    }

    fun noteToSelf() {
        valuePairs += CardValuePairs.NoteToSelf()
    }

    fun build() = Card(valuePairs)

    fun isValid() = valuePairs.isNotEmpty()

}

@CardsBuilderDSL
class CardsBuilder {

    private val cards = mutableListOf<Card>()

    operator fun Card.unaryPlus() {
        cards += this
    }

    fun card(setup: CardBuilder.() -> Unit = {}) {
        val cardBuilder = CardBuilder()
        cardBuilder.setup()

        if (cardBuilder.isValid()) {
            cards += cardBuilder.build()
        }
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
    fun cards(param: () -> Unit = {}) {}

}

fun cards(setup: CardsBuilder.() -> Unit): List<Card> {
    val cardsBuilder = CardsBuilder()
    cardsBuilder.setup()
    return cardsBuilder.build()
}