package com.spiraclesoftware.androidsample.feature.transaction_list

import android.content.Context
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.domain.entity.Money
import com.spiraclesoftware.androidsample.domain.entity.Transaction
import com.spiraclesoftware.androidsample.domain.entity.TransactionStatusCode
import com.spiraclesoftware.androidsample.extension.stringOrNull
import com.spiraclesoftware.androidsample.formatter.*
import java.time.ZonedDateTime

class TransactionListFormatter(private val ctx: Context) {

    fun headerModel(
        dateTime: ZonedDateTime,
        contributionToBalance: Money
    ) = HeaderModel(
        date = dateTime.format(DateTimeFormat.PRETTY_DATE),
        contribution = MoneyFormat(contributionToBalance)
            .formatSigned(showSignWhenPositive = false)
    )

    fun transactionModel(transactions: List<Transaction>): List<TransactionModel> =
        transactions.map(::transactionModel)

    fun transactionModel(transaction: Transaction): TransactionModel = with(transaction) {
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

    fun emptyState(
        listIsEmpty: Boolean,
        filterIsActive: Boolean
    ): EmptyState? {
        if (!listIsEmpty) return null

        return if (filterIsActive) {
            EmptyState(
                image = R.drawable.ic_empty_search_results,
                caption = R.string.empty_state__no_results__caption,
                message = R.string.empty_state__no_results__message
            )
        } else {
            EmptyState(
                caption = R.string.empty_state__no_transactions__caption,
                message = R.string.empty_state__no_transactions__message
            )
        }
    }

}