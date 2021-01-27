package com.spiraclesoftware.androidsample.domain.entity

import com.spiraclesoftware.androidsample.domain.money
import org.junit.Assert.*
import org.junit.Test
import java.math.BigDecimal

class MoneyTest {

    //region Addition
    @Test
    fun addition_simple() {
        val first = money("1", "EUR")
        val second = money("99", "EUR")

        val sum = first.add(second.amount).amount
        assertEquals(BigDecimal("100"), sum)
    }

    @Test
    fun addition_large_sums() {
        val first = money("100000", "EUR")
        val second = money("123456", "EUR")

        val sum = first.add(second.amount).amount
        assertEquals(BigDecimal("223456"), sum)
    }

    @Test
    fun addition_decimals() {
        val first = money("1234.567", "EUR")
        val second = money("1.234", "EUR")

        val sum = first.add(second.amount).amount
        assertEquals(BigDecimal("1235.801"), sum)
    }
    //endregion

    //region Subtraction
    @Test
    fun subtraction_simple() {
        val first = money("100", "EUR")
        val second = money("99", "EUR")

        val sum = first.subtract(second.amount).amount
        assertEquals(BigDecimal("1"), sum)
    }

    @Test
    fun subtraction_large_sums() {
        val first = money("123456", "EUR")
        val second = money("100000", "EUR")

        val sum = first.subtract(second.amount).amount
        assertEquals(BigDecimal("23456"), sum)
    }

    @Test
    fun subtraction_decimals() {
        val first = money("1234.567", "EUR")
        val second = money("1.234", "EUR")

        val sum = first.subtract(second.amount).amount
        assertEquals(BigDecimal("1233.333"), sum)
    }
    //endregion

    //region Multiplication
    @Test
    fun multiplication_of_integers() {
        val first = money("100", "EUR")
        val second = money("42", "EUR")

        val sum = first.multiply(second.amount).amount
        assertEquals(BigDecimal("4200"), sum)
    }

    @Test
    fun multiplication_of_decimals() {
        val first = money("100", "EUR")
        val second = money("123.456", "EUR")

        val sum = first.multiply(second.amount).amount
        assertEquals(BigDecimal("12345.60"), sum)
    }
    //endregion

    //region Division
    @Test
    fun division_without_remainder() {
        val first = money("100", "EUR")
        val second = money("50", "EUR")

        val sum = first.divide(second.amount).amount
        assertEquals(BigDecimal("2"), sum)
    }

    @Test
    fun division_with_remainder() {
        val first = money("100", "EUR")
        val second = money("3", "EUR")

        val sum = first.divide(second.amount).amount
        assertEquals(BigDecimal("33.33333"), sum)
    }
    //endregion

    //region Negate
    @Test
    fun negate_a_positive_amount() {
        val money = money("100", "EUR")

        assertEquals(BigDecimal("-100"), money.negate().amount)
    }

    @Test
    fun negate_a_negative_amount() {
        val money = money("-100", "EUR")

        assertEquals(BigDecimal("100"), money.negate().amount)
    }
    //endregion

    //region Comparisons
    @Test
    fun amounts_are_equal() {
        val first = money("100", "EUR")
        val second = money("100.00", "EUR")

        assertTrue(first.amountEquals(second))
    }

    @Test
    fun amounts_are_not_equal() {
        val first = money("100", "EUR")
        val second = money("500", "EUR")

        assertFalse(first.amountEquals(second))
    }
    //endregion
}