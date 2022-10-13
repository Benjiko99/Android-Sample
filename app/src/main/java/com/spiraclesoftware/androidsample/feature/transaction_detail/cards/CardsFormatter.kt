package com.spiraclesoftware.androidsample.feature.transaction_detail.cards

import android.content.Context
import androidx.core.net.toUri
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.common.formatter.TransactionCategoryFormatter
import com.spiraclesoftware.androidsample.common.formatter.TransactionStatusCodeFormatter
import com.spiraclesoftware.androidsample.common.formatter.TransactionStatusFormatter
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.item.model.*
import com.spiraclesoftware.androidsample.framework.core.Model
import com.spiraclesoftware.androidsample.framework.extensions.string

class CardsFormatter(
    private val ctx: Context,
    private val statusFormatter: TransactionStatusFormatter,
    private val statusCodeFormatter: TransactionStatusCodeFormatter,
    private val categoryFormatter: TransactionCategoryFormatter
) {

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
                        value = ctx.string(statusFormatter.stringRes(pair.status))
                    )
                is ValuePair.CardDescription ->
                    ValuePairModel(
                        label = R.string.transaction_detail__card,
                        value = pair.cardDescription.orEmpty(),
                        icon = R.drawable.ic_credit_card,
                        actionId = R.id.action_open_card_detail
                    )
                is ValuePair.DownloadStatement ->
                    ValuePairModel(
                        label = R.string.transaction_detail__statement,
                        value = ctx.string(R.string.transaction_detail__download),
                        icon = R.drawable.ic_download_statement,
                        actionId = R.id.action_download_statement
                    )
            }
        }
    }

}