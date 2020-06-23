package com.spiraclesoftware.androidsample.shared.ui

import com.spiraclesoftware.androidsample.shared.domain.Money
import java.text.DecimalFormat
import java.text.NumberFormat

class MoneyFormat(val money: Money) {

    private val formatter = (NumberFormat.getNumberInstance() as DecimalFormat).apply {
        currency = money.currency

        // Don't show the decimal numbers if they're not useful.
        if (money.amount.scale() > 0)
            applyPattern("¤#,##0.00")
        else
            applyPattern("¤#,##0")
    }

    /** Formats the sum in a plain way without any preceding signs. */
    fun formatUnsigned(): String {
        return formatter.format(money.amount.abs())
    }

    /** Formats the sum and adds a symbol for the sign (positive, negative). */
    fun formatSigned(showSignWhenPositive: Boolean = true): String {
        val formattedAmount = formatUnsigned()
        return if (money.amount.signum() >= 0) {
            if (showSignWhenPositive) "+ $formattedAmount" else formattedAmount
        } else {
            "- $formattedAmount"
        }
    }
}
