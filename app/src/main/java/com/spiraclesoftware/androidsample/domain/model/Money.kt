package com.spiraclesoftware.androidsample.domain.model

import com.spiraclesoftware.androidsample.ui.shared.MoneyFormat
import java.math.BigDecimal
import java.math.MathContext
import java.util.*

data class Money(
    val amount: BigDecimal,
    val currency: Currency
) {
    constructor(amount: String, currency: String) : this(BigDecimal(amount), Currency.getInstance(currency))

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

    /** Formats the sum in a plain way without any preceding signs. */
    fun formatUnsigned(): String = MoneyFormat(this).formatUnsigned()

    /** Formats the sum and adds a symbol for the sign (positive, negative). */
    fun formatSigned(showSignWhenPositive: Boolean = true): String =
        MoneyFormat(this).formatSigned(showSignWhenPositive)

    //region Math
    fun add(augend: BigDecimal) = copy(amount = amount.add(augend, mathContext))

    fun subtract(subtrahend: BigDecimal) = copy(amount = amount.subtract(subtrahend, mathContext))

    fun multiply(multiplicand: BigDecimal) = copy(amount = amount.multiply(multiplicand, mathContext))

    fun divide(divisor: BigDecimal) = copy(amount = amount.divide(divisor, mathContext))

    fun negate() = copy(amount = amount.negate(mathContext))
    //endregion
}
