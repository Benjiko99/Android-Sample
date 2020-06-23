package com.spiraclesoftware.androidsample.shared.domain

import java.math.BigDecimal
import java.math.MathContext
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

data class Money(
    val amount: BigDecimal,
    val currency: Currency
) {
    constructor(amount: String, currency: String)
            : this(BigDecimal(amount), Currency.getInstance(currency))

    companion object {
        val mathContext = MathContext.DECIMAL32!!
    }

    fun convertToCurrency(
        newCurrency: CurrencyCode,
        rates: ConversionRates
    ): Money {
        if (currency.currencyCode() == newCurrency) {
            return this
        }

        val conversionRate = rates.rates.find { it.currency == currency }!!.rate.toBigDecimal()
        return this.divide(conversionRate).copy(currency = newCurrency.toInstance())
    }

    private val formatter = (NumberFormat.getNumberInstance() as DecimalFormat).apply {
        currency = this@Money.currency

        // Don't show the decimal numbers if they're not useful.
        if (amount.scale() > 0)
            applyPattern("¤#,##0.00")
        else
            applyPattern("¤#,##0")
    }

    /**
     * Formats the sum in a plain way without any preceding signs.
     */
    fun formatUnsigned(): String {
        return formatter.format(amount.abs())
    }

    /**
     * Formats the sum and adds a symbol for the sign (positive, negative).
     */
    fun formatSigned(showSignWhenPositive: Boolean = true): String {
        val unsigned = formatUnsigned()
        return if (amount.signum() >= 0) {
            if (showSignWhenPositive) "+ $unsigned" else unsigned
        } else {
            "- $unsigned"
        }
    }

    //region Math
    fun add(augend: BigDecimal) = copy(amount = amount.add(augend, mathContext))

    fun subtract(subtrahend: BigDecimal) = copy(amount = amount.subtract(subtrahend, mathContext))

    fun multiply(multiplicand: BigDecimal) = copy(amount = amount.multiply(multiplicand, mathContext))

    fun divide(divisor: BigDecimal) = copy(amount = amount.divide(divisor, mathContext))

    fun negate() = copy(amount = amount.negate(mathContext))
    //endregion
}
