package com.spiraclesoftware.androidsample.ui.transactiondetail.cards

import android.content.Context
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.ui.transactiondetail.cards.items.NoteCardItem
import com.spiraclesoftware.androidsample.ui.transactiondetail.cards.items.StatusCardItem
import com.spiraclesoftware.androidsample.ui.transactiondetail.cards.items.ValuePairCardItem

sealed class Card

class ValuePairCard(val valuePairs: List<ValuePair>) : Card() {

    fun toItemData(ctx: Context, transaction: Transaction): List<ValuePairCardItem.Data> {
        return valuePairs.map {
            it.toItemData(ctx, transaction)
        }
    }

    fun toListItem(transaction: Transaction, actionsHandler: CardActionsHandler) =
        ValuePairCardItem(this, transaction, actionsHandler)

}

object StatusCard : Card() {

    fun toItemData(transaction: Transaction): StatusCardItem.Data {
        return StatusCardItem.Data(transaction.status, transaction.statusCode)
    }

    fun toListItem(transaction: Transaction) =
        StatusCardItem(this, transaction)

}

object NoteCard : Card() {

    fun toItemData(transaction: Transaction): NoteCardItem.Data {
        return NoteCardItem.Data(transaction.noteToSelf)
    }

    fun toListItem(transaction: Transaction, actionsHandler: CardActionsHandler) =
        NoteCardItem(this, transaction, actionsHandler)

}
