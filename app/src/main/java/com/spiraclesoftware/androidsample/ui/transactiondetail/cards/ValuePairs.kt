package com.spiraclesoftware.androidsample.ui.transactiondetail.cards

import android.content.Context
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.ui.transactiondetail.cards.items.ValuePairCardItem
import com.spiraclesoftware.core.extensions.drawable
import com.spiraclesoftware.core.extensions.string

abstract class ValuePair {
    abstract fun toItemData(ctx: Context, transaction: Transaction): ValuePairCardItem.Data
}

object ValuePairs {

    class Status : ValuePair() {

        override fun toItemData(ctx: Context, transaction: Transaction): ValuePairCardItem.Data {
            return ValuePairCardItem.Data(
                label = ctx.string(R.string.transaction__detail__status),
                value = ctx.string(transaction.status.stringRes)
            )
        }

    }

    class PaymentCard : ValuePair() {

        override fun toItemData(ctx: Context, transaction: Transaction): ValuePairCardItem.Data {
            return ValuePairCardItem.Data(
                label = ctx.string(R.string.transaction__detail__card),
                value = transaction.cardDescription.orEmpty(),
                icon = ctx.drawable(R.drawable.ic_credit_card),
                onClickAction = { it.onCardAction() }
            )
        }

    }

    class Statement : ValuePair() {

        override fun toItemData(ctx: Context, transaction: Transaction): ValuePairCardItem.Data {
            return ValuePairCardItem.Data(
                label = ctx.string(R.string.transaction__detail__statement),
                value = ctx.string(R.string.transaction__detail__download),
                icon = ctx.drawable(R.drawable.ic_download_statement),
                onClickAction = { it.onStatementAction() }
            )
        }

    }

    class Category : ValuePair() {

        override fun toItemData(ctx: Context, transaction: Transaction): ValuePairCardItem.Data {
            return ValuePairCardItem.Data(
                label = ctx.string(R.string.transaction__detail__category),
                value = ctx.string(transaction.category.stringRes),
                icon = ctx.drawable(transaction.category.drawableRes),
                onClickAction = { it.onCategoryAction() }
            )
        }

    }

    class Attachments : ValuePair() {

        override fun toItemData(ctx: Context, transaction: Transaction): ValuePairCardItem.Data {
            return ValuePairCardItem.Data(
                label = ctx.string(R.string.transaction__detail__attachments),
                value = ctx.string(R.string.transaction__detail__attachments__add_photo),
                icon = ctx.drawable(R.drawable.ic_add_photo),
                onClickAction = { it.onAttachmentAction() }
            )
        }

    }

}
