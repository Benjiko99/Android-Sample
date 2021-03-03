package com.spiraclesoftware.androidsample.feature.transaction_detail.cards

import androidx.core.net.toUri
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.item.model.*
import com.spiraclesoftware.androidsample.formatter.drawableRes
import com.spiraclesoftware.androidsample.formatter.stringRes
import com.spiraclesoftware.androidsample.framework.Model
import com.spiraclesoftware.androidsample.framework.StringHolder

class CardsFormatter {

    fun cardModels(cards: List<Card>): List<Model> {
        return cards.map { card ->
            when (card) {
                is ValuePairCard ->
                    ValuePairCardModel(card.valuePairs.toModels())
                is StatusCard ->
                    StatusCardModel(card.status.stringRes, card.statusCode.stringRes)
                is CategoryCard ->
                    CategoryCardModel(card.category.stringRes, card.category.drawableRes)
                is AttachmentsCard ->
                    AttachmentsCardModel(card.attachments.map(String::toUri), card.uploads)
                is NoteCard ->
                    NoteCardModel(card.noteToSelf)
            }
        }
    }

    private fun List<ValuePair>.toModels(): List<ValuePairModel> {
        return map { pair ->
            when (pair) {
                is ValuePair.Status ->
                    ValuePairModel(
                        label = R.string.transaction_detail__status,
                        value = StringHolder(pair.status.stringRes)
                    )
                is ValuePair.CardDescription ->
                    ValuePairModel(
                        label = R.string.transaction_detail__card,
                        value = StringHolder(pair.cardDescription.orEmpty()),
                        icon = R.drawable.ic_credit_card,
                        onClickAction = { it.onOpenCardDetail() }
                    )
                is ValuePair.DownloadStatement ->
                    ValuePairModel(
                        label = R.string.transaction_detail__statement,
                        value = StringHolder(R.string.transaction_detail__download),
                        icon = R.drawable.ic_download_statement,
                        onClickAction = { it.onDownloadStatement() }
                    )
            }
        }
    }

}