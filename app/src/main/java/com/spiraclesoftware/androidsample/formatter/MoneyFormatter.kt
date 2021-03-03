package com.spiraclesoftware.androidsample.formatter

import com.spiraclesoftware.androidsample.domain.entity.Money
import com.spiraclesoftware.androidsample.domain.entity.Transaction
import java.text.DecimalFormat
import java.text.NumberFormat

class MoneyFormatter {

    /** Formats using the most common way used across the app. */
    fun format(transaction: Transaction): String {
        return if (transaction.contributesToAccountBalance()) {
            formatSigned(transaction.signedMoney)
        } else {
            formatUnsigned(transaction.money)
        }
    }

    /** Formats the amount in a plain way without any preceding signs. */
    fun formatUnsigned(money: Money): String {
        return getNumberFormat(money).format(money.amount.abs())
    }

    /** Formats the amount and adds a symbol for the sign (positive, negative). */
    fun formatSigned(money: Money, showSignWhenPositive: Boolean = true): String {
        val formattedAmount = formatUnsigned(money)
        return if (money.amount.signum() >= 0) {
            if (showSignWhenPositive) "+ $formattedAmount" else formattedAmount
        } else {
            "- $formattedAmount"
        }
    }

    private fun getNumberFormat(money: Money): NumberFormat {
        return (NumberFormat.getNumberInstance() as DecimalFormat).apply {
            currency = money.currency

            // Don't show the decimal numbers if its just zeros.
            if (money.amount.scale() > 0)
                applyPattern("¤#,##0.00")
            else
                applyPattern("¤#,##0")
        }
    }

}
