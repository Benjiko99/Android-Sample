package com.spiraclesoftware.androidsample.feature.transaction_detail.cards

import android.net.Uri
import androidx.core.net.toUri
import com.spiraclesoftware.androidsample.domain.entity.Transaction
import com.spiraclesoftware.androidsample.domain.entity.TransactionCategory
import com.spiraclesoftware.androidsample.domain.entity.TransactionStatus
import com.spiraclesoftware.androidsample.domain.entity.TransactionStatusCode
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.item.model.*

sealed class Card

class ValuePairCard(val valuePairs: List<ValuePair>) : Card() {

    fun toModel(transaction: Transaction) =
        ValuePairCardModel(
            valuePairs.map { it.toModel(transaction) }
        )

}

object StatusCard : Card() {

    fun toModel(
        status: TransactionStatus,
        statusCode: TransactionStatusCode
    ) = StatusCardModel(status, statusCode)

}

object CategoryCard : Card() {

    fun toModel(category: TransactionCategory) =
        CategoryCardModel(category)

}

object AttachmentsCard : Card() {

    fun toModel(
        attachments: List<String>,
        uploads: List<Uri>
    ) = AttachmentsCardModel(attachments.map(String::toUri), uploads)

}

object NoteCard : Card() {

    fun toModel(noteToSelf: String?) =
        NoteCardModel(noteToSelf)

}
