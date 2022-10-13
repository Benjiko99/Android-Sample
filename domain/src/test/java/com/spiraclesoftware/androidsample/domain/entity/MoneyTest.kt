package com.spiraclesoftware.androidsample.domain.entity

import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.domain.money
import org.junit.Test
import java.math.BigDecimal

class MoneyTest {

    //region Addition
    @Test
    fun addition_simple() {
        val first = money("1", "EUR")
        val second = money("99", "EUR")

        val sum = first.add(second.amount).amount
        assertThat(sum).isEqualTo(BigDecimal("100"))
    }

    @Test
    fun addition_large_sums() {
        val first = money("100000", "EUR")
        val second = money("123456", "EUR")

        val sum = first.add(second.amount).amount
        assertThat(sum).isEqualTo(BigDecimal("223456"))
    }

    @Test
    fun addition_decimals() {
        val first = money("1234.567", "EUR")
        val second = money("1.234", "EUR")

        val sum = first.add(second.amount).amount
        assertThat(sum).isEqualTo(BigDecimal("1235.801"))
    }
    //endregion

    //region Subtraction
    @Test
    fun subtraction_simple() {
        val first = money("100", "EUR")
        val second = money("99", "EUR")

        val sum = first.subtract(second.amount).amount
        assertThat(sum).isEqualTo(BigDecimal("1"))
    }

    @Test
    fun subtraction_large_sums() {
        val first = money("123456", "EUR")
        val second = money("100000", "EUR")

        val sum = first.subtract(second.amount).amount
        assertThat(sum).isEqualTo(BigDecimal("23456"))
    }

    @Test
    fun subtraction_decimals() {
        val first = money("1234.567", "EUR")
        val second = money("1.234", "EUR")

        val sum = first.subtract(second.amount).amount
        assertThat(sum).isEqualTo(BigDecimal("1233.333"))
    }
    //endregion

    //region Multiplication
    @Test
    fun multiplication_of_integers() {
        val first = money("100", "EUR")
        val second = money("42", "EUR")

        val sum = first.multiply(second.amount).amount
        assertThat(sum).isEqualTo(BigDecimal("4200"))
    }

    @Test
    fun multiplication_of_decimals() {
        val first = money("100", "EUR")
        val second = money("123.456", "EUR")

        val sum = first.multiply(second.amount).amount
        assertThat(sum).isEqualTo(BigDecimal("12345.60"))
    }
    //endregion

    //region Division
    @Test
    fun division_without_remainder() {
        val first = money("100", "EUR")
        val second = money("50", "EUR")

        val sum = first.divide(second.amount).amount
        assertThat(sum).isEqualTo(BigDecimal("2"))
    }

    @Test
    fun division_with_remainder() {
        val first = money("100", "EUR")
        val second = money("3", "EUR")

        val sum = first.divide(second.amount).amount
        assertThat(sum).isEqualTo(BigDecimal("33.33333"))
    }
    //endregion

    //region Negate
    @Test
    fun negate_a_positive_amount() {
        val money = money("100", "EUR")

        assertThat(money.negate().amount).isEqualTo(BigDecimal("-100"))
    }

    @Test
    fun negate_a_negative_amount() {
        val money = money("-100", "EUR")

        assertThat(money.negate().amount).isEqualTo(BigDecimal("100"))
    }
    //endregion

    //region Comparisons
    @Test
    fun amounts_are_equal() {
        val first = money("100", "EUR")
        val second = money("100.00", "EUR")

        assertThat(first.amountEquals(second)).isTrue()
    }

    @Test
    fun amounts_are_not_equal() {
        val first = money("100", "EUR")
        val second = money("500", "EUR")

        assertThat(first.amountEquals(second)).isFalse()
    }
    //endregion
}