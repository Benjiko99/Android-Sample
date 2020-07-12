package com.spiraclesoftware.androidsample.ui.transactiondetail.cards

import android.content.Context
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.ui.transactiondetail.CardItemData
import com.spiraclesoftware.androidsample.shared.domain.Transaction
import com.spiraclesoftware.core.extensions.drawable
import com.spiraclesoftware.core.extensions.string

abstract class CardValuePair {

    abstract fun toItemData(ctx: Context, transaction: Transaction): CardItemData
}

object CardValuePairs {

    class TransactionStatus(private val expanded: Boolean) : CardValuePair() {

        override fun toItemData(ctx: Context, transaction: Transaction): CardItemData {
            return when (expanded) {
                false -> CardItemData(
                    label = ctx.string(R.string.transaction__detail__status),
                    value = ctx.string(transaction.status.stringRes)
                )
                true -> CardItemData(
                    label = ctx.string(R.string.transaction__detail__status),
                    body = ctx.string(transaction.status.stringRes) + " ∙ " + ctx.string(transaction.statusCode.stringRes!!)
                )
            }
        }
    }

    class CardDescription : CardValuePair() {

        override fun toItemData(ctx: Context, transaction: Transaction): CardItemData {
            return CardItemData(
                label = ctx.string(R.string.transaction__detail__card),
                value = transaction.cardDescription.orEmpty(),
                icon = ctx.drawable(R.drawable.ic_credit_card),
                actionId = R.id.card_action__card_detail
            )
        }
    }

    class TransactionStatement : CardValuePair() {

        override fun toItemData(ctx: Context, transaction: Transaction): CardItemData {
            return CardItemData(
                label = ctx.string(R.string.transaction__detail__statement),
                value = ctx.string(R.string.transaction__detail__download),
                icon = ctx.drawable(R.drawable.ic_download_statement),
                actionId = R.id.card_action__download_statement
            )
        }
    }

    class TransactionCategory : CardValuePair() {

        override fun toItemData(ctx: Context, transaction: Transaction): CardItemData {
            return CardItemData(
                label = ctx.string(R.string.transaction__detail__category),
                value = ctx.string(transaction.category.stringRes),
                icon = ctx.drawable(transaction.category.drawableRes),
                actionId = R.id.card_action__change_category
            )
        }
    }

    class NoteToSelf : CardValuePair() {

        override fun toItemData(ctx: Context, transaction: Transaction): CardItemData {
            return CardItemData(
                label = ctx.string(R.string.transaction__detail__note),
                value = if (transaction.noteToSelf == null)
                    ctx.string(R.string.transaction__detail__note__add)
                else
                    ctx.string(R.string.transaction__detail__note__edit),
                body = transaction.noteToSelf,
                icon = ctx.drawable(R.drawable.ic_edit_note),
                actionId = R.id.card_action__change_note
            )
        }
    }
}
