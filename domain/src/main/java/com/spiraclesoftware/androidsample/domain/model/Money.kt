package com.spiraclesoftware.androidsample.domain.model

import java.math.BigDecimal
import java.math.MathContext
import java.util.*

data class Money(
    val amount: BigDecimal,
    val currency: Currency
) {

    companion object {
        /** Keep the MathContext consistent across the application */
        val mathContext = MathContext.DECIMAL32!!
    }

    fun add(augend: BigDecimal) =
        copy(amount = amount.add(augend, mathContext))

    fun subtract(subtrahend: BigDecimal) =
        copy(amount = amount.subtract(subtrahend, mathContext))

    fun multiply(multiplicand: BigDecimal) =
        copy(amount = amount.multiply(multiplicand, mathContext))

    fun divide(divisor: BigDecimal) =
        copy(amount = amount.divide(divisor, mathContext))

    fun negate() =
        copy(amount = amount.negate(mathContext))

    /**
     * Compares the effective amounts, ignoring things like extra zeros,
     * which regular equals() would take into account.
     *
     * @return true when comparing 1.0 against 1.00
     */
    fun amountEquals(amount: BigDecimal): Boolean {
        return this.amount.compareTo(amount) == 0
    }

    fun amountEquals(other: Money): Boolean {
        return this.amountEquals(other.amount)
    }

}
