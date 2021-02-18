package com.spiraclesoftware.androidsample.formatter

import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.money
import org.junit.Test

class MoneyFormatTest {

    @Test
    fun signed() {
        val money = money("100", "EUR")
        val moneyFormat = MoneyFormat(money)
        assertThat(moneyFormat.formatSigned()).isEqualTo("+ EUR100")
    }

    @Test
    fun signed_decimal() {
        val money = money("12345.67", "EUR")
        val moneyFormat = MoneyFormat(money)
        assertThat(moneyFormat.formatSigned()).isEqualTo("+ EUR12,345.67")
    }

    @Test
    fun signed_decimal_with_zeros() {
        val money = money("100.00", "EUR")
        val moneyFormat = MoneyFormat(money)
        assertThat(moneyFormat.formatSigned()).isEqualTo("+ EUR100.00")
    }

    @Test
    fun signed_without_positive_sign() {
        val money = money("100", "EUR")
        val moneyFormat = MoneyFormat(money)
        assertThat(moneyFormat.formatSigned(showSignWhenPositive = false)).isEqualTo("EUR100")
    }

    @Test
    fun unsigned() {
        val money = money("100", "EUR")
        val moneyFormat = MoneyFormat(money)
        assertThat(moneyFormat.formatUnsigned()).isEqualTo("EUR100")
    }

    @Test
    fun unsigned_negative() {
        val money = money("-100", "EUR")
        val moneyFormat = MoneyFormat(money)
        assertThat(moneyFormat.formatUnsigned()).isEqualTo("EUR100")
    }

    @Test
    fun signed_negative() {
        val money = money("-100", "EUR")
        val moneyFormat = MoneyFormat(money)
        assertThat(moneyFormat.formatSigned()).isEqualTo("- EUR100")
    }
}