package com.spiraclesoftware.androidsample.feature.transaction_detail.cards

import android.net.Uri
import androidx.core.net.toUri
import com.spiraclesoftware.androidsample.domain.entity.Transaction
import com.spiraclesoftware.androidsample.domain.entity.TransactionCategory
import com.spiraclesoftware.androidsample.domain.entity.TransactionStatus
import com.spiraclesoftware.androidsample.domain.entity.TransactionStatusCode
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.items.*

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

    fun toListItem(attachments: List<String>, uploads: List<Uri>, actionsHandler: CardActionsHandler) =
        AttachmentsCardItem(
            AttachmentsCardItem.Data(attachments.map(String::toUri), uploads),
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
