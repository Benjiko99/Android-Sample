package com.spiraclesoftware.androidsample.feature.transaction_detail

import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.domain.entity.Transaction
import com.spiraclesoftware.androidsample.formatter.DateTimeFormat
import com.spiraclesoftware.androidsample.formatter.MoneyFormatter
import com.spiraclesoftware.androidsample.formatter.TransactionCategoryFormatter
import com.spiraclesoftware.androidsample.framework.Model
import org.koin.java.KoinJavaComponent.inject

class TransactionDetailFormatter {

    private val categoryFormatter by inject(TransactionCategoryFormatter::class.java)

    fun detailModel(
        transaction: Transaction,
        cardModels: List<Model>
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
            isSuccessful = isSuccessful(),
            cardModels = cardModels
        )
    }

}