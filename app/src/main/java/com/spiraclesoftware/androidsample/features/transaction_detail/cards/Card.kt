package com.spiraclesoftware.androidsample.features.transaction_detail.cards

import android.net.Uri
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.domain.model.TransactionCategory
import com.spiraclesoftware.androidsample.domain.model.TransactionStatus
import com.spiraclesoftware.androidsample.domain.model.TransactionStatusCode
import com.spiraclesoftware.androidsample.features.transaction_detail.cards.items.*

sealed class Card

class ValuePairCard(val valuePairs: List<ValuePair>) : Card() {

    fun toListItem(transaction: Transaction, actionsHandler: CardActionsHandler) =
        ValuePairCardItem(
            valuePairs.map { it.toItemData(transaction) },
            actionsHandler
        )
}

object StatusCard : Card() {

    fun toListItem(status: TransactionStatus, statusCode: TransactionStatusCode) =
        StatusCardItem(
            StatusCardItem.Data(status, statusCode)
        )
}

object CategoryCard : Card() {

    fun toListItem(category: TransactionCategory, actionsHandler: CardActionsHandler) =
        CategoryCardItem(
            CategoryCardItem.Data(category),
            actionsHandler
        )
}

object AttachmentsCard : Card() {

    fun toListItem(attachments: List<Uri>, uploads: List<Uri>, actionsHandler: CardActionsHandler) =
        AttachmentsCardItem(
            AttachmentsCardItem.Data(attachments, uploads),
            actionsHandler
        )
}

object NoteCard : Card() {

    fun toListItem(noteToSelf: String?, actionsHandler: CardActionsHandler) =
        NoteCardItem(
            NoteCardItem.Data(noteToSelf),
            actionsHandler
        )
}
