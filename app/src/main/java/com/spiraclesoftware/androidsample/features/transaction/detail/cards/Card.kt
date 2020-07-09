package com.spiraclesoftware.androidsample.features.transaction.detail.cards

import android.content.Context
import com.spiraclesoftware.androidsample.features.transaction.detail.CardItem
import com.spiraclesoftware.androidsample.features.transaction.detail.CardItemData
import com.spiraclesoftware.androidsample.shared.domain.Transaction

/**
 * A declaration of value-pairs that will be making up the [CardItem] in a list.
 */
class Card internal constructor(
    private val valuePairs: List<CardValuePair>
) {

    fun toItemData(ctx: Context, transaction: Transaction): List<CardItemData> {
        return valuePairs.map {
            it.toItemData(ctx, transaction)
        }
    }
}

class CardBuilder {

    private val valuePairs: MutableList<CardValuePair> = arrayListOf()

    fun with(valuePair: CardValuePair) {
        valuePairs.add(valuePair)
    }

    fun build(): Card? {
        if (valuePairs.isEmpty()) return null

        return Card(valuePairs)
    }
}