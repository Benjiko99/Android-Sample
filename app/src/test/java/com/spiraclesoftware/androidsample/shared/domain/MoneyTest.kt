package com.spiraclesoftware.androidsample.shared.domain

import com.spiraclesoftware.androidsample.TestData
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class MoneyTest {

    @Test
    fun currency_conversion() {
        val eur = Money("10", "EUR")
        val usd = Money("10", "USD")

        val usdToEur = usd.convertToCurrency(CurrencyCode("EUR"), TestData.conversionRates)
        val sum = eur.add(usdToEur.amount)

        assertEquals(BigDecimal("8.928571"), usdToEur.amount)
        assertEquals(Money("18.92857", "EUR"), sum)
    }

    //region Addition
    @Test
    fun addition_simple() {
        val first = Money("1", "EUR")
        val second = Money("99", "EUR")

        val sum = first.add(second.amount).amount
        assertEquals(BigDecimal("100"), sum)
    }

    @Test
    fun addition_large_sums() {
        val first = Money("100000", "EUR")
        val second = Money("123456", "EUR")

        val sum = first.add(second.amount).amount
        assertEquals(BigDecimal("223456"), sum)
    }

    @Test
    fun addition_decimals() {
        val first = Money("1234.567", "EUR")
        val second = Money("1.234", "EUR")

        val sum = first.add(second.amount).amount
        assertEquals(BigDecimal("1235.801"), sum)
    }
    //endregion

    //region Subtraction
    @Test
    fun subtraction_simple() {
        val first = Money("100", "EUR")
        val second = Money("99", "EUR")

        val sum = first.subtract(second.amount).amount
        assertEquals(BigDecimal("1"), sum)
    }

    @Test
    fun subtraction_large_sums() {
        val first = Money("123456", "EUR")
        val second = Money("100000", "EUR")

        val sum = first.subtract(second.amount).amount
        assertEquals(BigDecimal("23456"), sum)
    }

    @Test
    fun subtraction_decimals() {
        val first = Money("1234.567", "EUR")
        val second = Money("1.234", "EUR")

        val sum = first.subtract(second.amount).amount
        assertEquals(BigDecimal("1233.333"), sum)
    }
    //endregion

    //region Multiplication
    @Test
    fun multiplication_of_integers() {
        val first = Money("100", "EUR")
        val second = Money("42", "EUR")

        val sum = first.multiply(second.amount).amount
        assertEquals(BigDecimal("4200"), sum)
    }

    @Test
    fun multiplication_of_decimals() {
        val first = Money("100", "EUR")
        val second = Money("123.456", "EUR")

        val sum = first.multiply(second.amount).amount
        assertEquals(BigDecimal("12345.60"), sum)
    }
    //endregion

    //region Division
    @Test
    fun division_without_remainder() {
        val first = Money("100", "EUR")
        val second = Money("50", "EUR")

        val sum = first.divide(second.amount).amount
        assertEquals(BigDecimal("2"), sum)
    }

    @Test
    fun division_with_remainder() {
        val first = Money("100", "EUR")
        val second = Money("3", "EUR")

        val sum = first.divide(second.amount).amount
        assertEquals(BigDecimal("33.33333"), sum)
    }
    //endregion
}