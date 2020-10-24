package com.spiraclesoftware.androidsample.ui.transactiondetail.cards

import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.ui.transactiondetail.cards.items.*

sealed class Card {
    abstract fun toListItem(transaction: Transaction, actionsHandler: CardActionsHandler): CardItem<*>
}

class ValuePairCard(val valuePairs: List<ValuePair>) : Card() {

    override fun toListItem(transaction: Transaction, actionsHandler: CardActionsHandler) =
        ValuePairCardItem(
            valuePairs.map { it.toItemData(transaction) },
            actionsHandler
        )
}

object StatusCard : Card() {

    override fun toListItem(transaction: Transaction, actionsHandler: CardActionsHandler) =
        StatusCardItem(
            StatusCardItem.Data(transaction.status, transaction.statusCode)
        )
}

object CategoryCard : Card() {

    override fun toListItem(transaction: Transaction, actionsHandler: CardActionsHandler) =
        CategoryCardItem(
            CategoryCardItem.Data(transaction.category),
            actionsHandler
        )
}

object AttachmentsCard : Card() {

    override fun toListItem(transaction: Transaction, actionsHandler: CardActionsHandler) =
        AttachmentsCardItem(
            AttachmentsCardItem.Data(transaction.attachments),
            actionsHandler
        )
}

object NoteCard : Card() {

    override fun toListItem(transaction: Transaction, actionsHandler: CardActionsHandler) =
        NoteCardItem(
            NoteCardItem.Data(transaction.noteToSelf),
            actionsHandler
        )
}
