package com.spiraclesoftware.androidsample.feature.transaction_detail

import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.common.DateTimeFormat
import com.spiraclesoftware.androidsample.common.formatter.MoneyFormatter
import com.spiraclesoftware.androidsample.common.formatter.TransactionCategoryFormatter
import com.spiraclesoftware.androidsample.domain.entity.Transaction

class TransactionDetailFormatter(
    private val categoryFormatter: TransactionCategoryFormatter,
) {

    fun detailModel(
        transaction: Transaction
    ): DetailModel = with(transaction) {
        val iconRes: Int
        val iconTintRes: Int

        if (isSuccessful()) {
            iconTintRes = categoryFormatter.colorRes(category)
            iconRes = categoryFormatter.drawableRes(category)
        } else {
            iconTintRes = R.color.transaction_status__declined
            iconRes = R.drawable.ic_status_declined
        }

        return DetailModel(
            id = id,
            name = name,
            formattedMoney = MoneyFormatter().format(this),
            processingDate = processingDate.format(DateTimeFormat.PRETTY_DATE_TIME),
            iconRes = iconRes,
            iconTintRes = iconTintRes,
            contributesToBalance = contributesToAccountBalance(),
            isSuccessful = isSuccessful()
        )
    }

}