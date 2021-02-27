package com.spiraclesoftware.androidsample.feature.transaction_detail.cards

import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.domain.entity.Transaction
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.item.model.ValuePairModel
import com.spiraclesoftware.androidsample.formatter.stringRes
import com.spiraclesoftware.androidsample.framework.StringHolder

abstract class ValuePair {
    abstract fun toModel(transaction: Transaction): ValuePairModel
}

object ValuePairs {

    class Status : ValuePair() {

        override fun toModel(transaction: Transaction) =
            ValuePairModel(
                label = R.string.transaction_detail__status,
                value = StringHolder(transaction.status.stringRes)
            )

    }

    class CardDescription : ValuePair() {

        override fun toModel(transaction: Transaction) =
            ValuePairModel(
                label = R.string.transaction_detail__card,
                value = StringHolder(transaction.cardDescription.orEmpty()),
                icon = R.drawable.ic_credit_card,
                onClickAction = { it.onOpenCardDetail() }
            )

    }

    class DownloadStatement : ValuePair() {

        override fun toModel(transaction: Transaction) =
            ValuePairModel(
                label = R.string.transaction_detail__statement,
                value = StringHolder(R.string.transaction_detail__download),
                icon = R.drawable.ic_download_statement,
                onClickAction = { it.onDownloadStatement() }
            )

    }

}
