package com.spiraclesoftware.androidsample.feature.transaction_list

import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.common.DateTimeFormat
import com.spiraclesoftware.androidsample.common.formatter.MoneyFormatter
import com.spiraclesoftware.androidsample.common.formatter.TransactionCategoryFormatter
import com.spiraclesoftware.androidsample.common.formatter.TransactionStatusCodeFormatter
import com.spiraclesoftware.androidsample.common.formatter.TransferDirectionFilterFormatter
import com.spiraclesoftware.androidsample.domain.entity.Money
import com.spiraclesoftware.androidsample.domain.entity.Transaction
import com.spiraclesoftware.androidsample.domain.entity.TransactionsFilter
import com.spiraclesoftware.androidsample.feature.transaction_list.item.model.HeaderModel
import com.spiraclesoftware.androidsample.feature.transaction_list.item.model.TransactionModel
import java.time.ZonedDateTime

class TransactionListFormatter(
    private val statusCodeFormatter: TransactionStatusCodeFormatter,
    private val categoryFormatter: TransactionCategoryFormatter,
    private val directionFilterFormatter: TransferDirectionFilterFormatter,
) {

    fun headerModel(
        dateTime: ZonedDateTime,
        contributionToBalance: Money
    ) = HeaderModel(
        date = dateTime.format(DateTimeFormat.PRETTY_DATE),
        contribution = MoneyFormatter()
            .formatSigned(contributionToBalance, showSignWhenPositive = false)
    )

    fun transactionModel(transactions: List<Transaction>): List<TransactionModel> =
        transactions.map(::transactionModel)

    fun transactionModel(transaction: Transaction): TransactionModel = with(transaction) {
        val iconRes: Int
        val iconTintRes: Int

        if (isSuccessful()) {
            iconTintRes = categoryFormatter.colorRes(category)
            iconRes = categoryFormatter.drawableRes(category)
        } else {
            iconTintRes = R.color.transaction_status__declined
            iconRes = R.drawable.ic_status_declined
        }

        return TransactionModel(
            id = id,
            name = name,
            iconRes = iconRes,
            iconTintRes = iconTintRes,
            amount = MoneyFormatter().format(this),
            processingDate = processingDate.format(DateTimeFormat.PRETTY_DATE_TIME),
            statusCodeRes = statusCodeFormatter.stringRes(statusCode),
            contributesToAccountBalance = contributesToAccountBalance()
        )
    }

    fun emptyState(
        listIsEmpty: Boolean,
        isFilterActive: Boolean
    ): EmptyState? {
        if (!listIsEmpty) return null

        return if (isFilterActive) {
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

    fun filterModel(filter: TransactionsFilter) =
        FilterModel(directionFilter = filter.directionFilter)

    fun filterStringIds() =
        directionFilterFormatter.listStringRes()

}