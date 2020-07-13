package com.spiraclesoftware.androidsample.ui.transactiondetail.cards

import android.content.Context
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.ui.transactiondetail.CardItem
import com.spiraclesoftware.androidsample.ui.transactiondetail.CardItemData

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
