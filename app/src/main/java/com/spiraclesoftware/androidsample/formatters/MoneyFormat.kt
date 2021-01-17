package com.spiraclesoftware.androidsample.formatters

import com.spiraclesoftware.androidsample.domain.model.Money
import com.spiraclesoftware.androidsample.domain.model.Transaction
import java.text.DecimalFormat
import java.text.NumberFormat

class MoneyFormat(val money: Money) {

    private val formatter = (NumberFormat.getNumberInstance() as DecimalFormat).apply {
        currency = money.currency

        // Don't show the decimal numbers if its just zeros.
        if (money.amount.scale() > 0)
            applyPattern("¤#,##0.00")
        else
            applyPattern("¤#,##0")
    }

    /** Formats using the most common way used across the app. */
    fun format(transaction: Transaction): String {
        return if (transaction.contributesToAccountBalance()) {
            formatSigned()
        } else {
            formatUnsigned()
        }
    }

    /** Formats the amount in a plain way without any preceding signs. */
    fun formatUnsigned(): String {
        return formatter.format(money.amount.abs())
    }

    /** Formats the amount and adds a symbol for the sign (positive, negative). */
    fun formatSigned(showSignWhenPositive: Boolean = true): String {
        val formattedAmount = formatUnsigned()
        return if (money.amount.signum() >= 0) {
            if (showSignWhenPositive) "+ $formattedAmount" else formattedAmount
        } else {
            "- $formattedAmount"
        }
    }

}
