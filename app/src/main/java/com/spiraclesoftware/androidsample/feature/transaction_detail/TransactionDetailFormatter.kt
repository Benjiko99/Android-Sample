package com.spiraclesoftware.androidsample.feature.transaction_detail

import android.net.Uri
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.domain.entity.Transaction
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.CardsPresenter
import com.spiraclesoftware.androidsample.formatter.DateTimeFormat
import com.spiraclesoftware.androidsample.formatter.MoneyFormat
import com.spiraclesoftware.androidsample.formatter.colorRes
import com.spiraclesoftware.androidsample.formatter.drawableRes

class TransactionDetailFormatter(
    private val cardsPresenter: CardsPresenter
) {

    fun detailModel(transaction: Transaction, uploads: List<Uri>): DetailModel = with(transaction) {
        val iconRes: Int
        val iconTintRes: Int

        if (isSuccessful()) {
            iconTintRes = category.colorRes
            iconRes = category.drawableRes
        } else {
            iconTintRes = R.color.transaction_status__declined
            iconRes = R.drawable.ic_status_declined
        }

        return DetailModel(
            id = id,
            name = name,
            formattedMoney = MoneyFormat(signedMoney).format(this),
            processingDate = processingDate.format(DateTimeFormat.PRETTY_DATE_TIME),
            iconRes = iconRes,
            iconTintRes = iconTintRes,
            contributesToBalance = contributesToAccountBalance(),
            isSuccessful = isSuccessful(),
            cardModels = cardsPresenter.cardModels(transaction, uploads)
        )
    }

}