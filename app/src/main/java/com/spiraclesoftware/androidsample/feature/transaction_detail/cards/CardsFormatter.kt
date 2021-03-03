package com.spiraclesoftware.androidsample.feature.transaction_detail.cards

import androidx.core.net.toUri
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.item.model.*
import com.spiraclesoftware.androidsample.formatter.TransactionCategoryFormatter
import com.spiraclesoftware.androidsample.formatter.TransactionStatusCodeFormatter
import com.spiraclesoftware.androidsample.formatter.TransactionStatusFormatter
import com.spiraclesoftware.androidsample.framework.Model
import com.spiraclesoftware.androidsample.framework.StringHolder
import org.koin.java.KoinJavaComponent.inject

class CardsFormatter {

    private val statusFormatter by inject(TransactionStatusFormatter::class.java)
    private val statusCodeFormatter by inject(TransactionStatusCodeFormatter::class.java)
    private val categoryFormatter by inject(TransactionCategoryFormatter::class.java)

    fun cardModels(cards: List<Card>): List<Model> {
        return cards.map { card ->
            when (card) {
                is ValuePairCard ->
                    ValuePairCardModel(card.valuePairs.toModels())
                is StatusCard ->
                    StatusCardModel(
                        statusFormatter.stringRes(card.status),
                        statusCodeFormatter.stringRes(card.statusCode)
                    )
                is CategoryCard ->
                    CategoryCardModel(
                        categoryFormatter.stringRes(card.category),
                        categoryFormatter.drawableRes(card.category)
                    )
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
                        value = StringHolder(statusFormatter.stringRes(pair.status))
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