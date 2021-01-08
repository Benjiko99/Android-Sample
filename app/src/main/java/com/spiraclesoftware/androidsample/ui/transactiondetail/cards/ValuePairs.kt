package com.spiraclesoftware.androidsample.ui.transactiondetail.cards

import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.ui.shared.stringRes
import com.spiraclesoftware.androidsample.ui.transactiondetail.cards.items.ValuePairCardItem
import com.spiraclesoftware.core.utils.StringHolder

abstract class ValuePair {
    abstract fun toItemData(transaction: Transaction): ValuePairCardItem.Data
}

object ValuePairs {

    class Status : ValuePair() {

        override fun toItemData(transaction: Transaction) =
            ValuePairCardItem.Data(
                label = R.string.transaction_detail__status,
                value = StringHolder(transaction.status.stringRes)
            )

    }

    class CardDescription : ValuePair() {

        override fun toItemData(transaction: Transaction) =
            ValuePairCardItem.Data(
                label = R.string.transaction_detail__card,
                value = StringHolder(transaction.cardDescription.orEmpty()),
                icon = R.drawable.ic_credit_card,
                onClickAction = { it.onOpenCardDetail() }
            )

    }

    class DownloadStatement : ValuePair() {

        override fun toItemData(transaction: Transaction) =
            ValuePairCardItem.Data(
                label = R.string.transaction_detail__statement,
                value = StringHolder(R.string.transaction_detail__download),
                icon = R.drawable.ic_download_statement,
                onClickAction = { it.onDownloadStatement() }
            )

    }

}
