package com.spiraclesoftware.androidsample.feature.transaction_list

import android.content.Context
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.domain.entity.Transaction
import com.spiraclesoftware.androidsample.domain.entity.TransactionStatusCode
import com.spiraclesoftware.androidsample.extension.stringOrNull
import com.spiraclesoftware.androidsample.formatter.*

class TransactionModelFormatter(private val ctx: Context) {

    fun format(transactions: List<Transaction>): List<TransactionModel> =
        transactions.map(this::format)

    fun format(transaction: Transaction): TransactionModel = with(transaction) {
        val iconRes: Int
        val iconTintRes: Int

        if (statusCode == TransactionStatusCode.SUCCESSFUL) {
            iconTintRes = category.colorRes
            iconRes = category.drawableRes
        } else {
            iconTintRes = R.color.transaction_status__declined
            iconRes = R.drawable.ic_status_declined
        }

        return TransactionModel(
            id = id,
            name = name,
            iconRes = iconRes,
            iconTintRes = iconTintRes,
            amount = MoneyFormat(signedMoney).format(this),
            processingDate = processingDate.format(DateTimeFormat.PRETTY_DATE_TIME),
            status = ctx.stringOrNull(statusCode.stringRes),
            contributesToAccountBalance = contributesToAccountBalance()
        )
    }

}