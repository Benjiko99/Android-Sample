package com.spiraclesoftware.androidsample.shared

import com.spiraclesoftware.androidsample.money
import junit.framework.Assert.assertEquals
import org.junit.Test

class MoneyFormatTest {

    @Test
    fun signed() {
        val money = money("100", "EUR")
        val moneyFormat = MoneyFormat(money)
        assertEquals("+ EUR100", moneyFormat.formatSigned())
    }

    @Test
    fun signed_decimal() {
        val money = money("12345.67", "EUR")
        val moneyFormat = MoneyFormat(money)
        assertEquals("+ EUR12,345.67", moneyFormat.formatSigned())
    }

    @Test
    fun signed_decimal_with_zeros() {
        val money = money("100.00", "EUR")
        val moneyFormat = MoneyFormat(money)
        assertEquals("+ EUR100.00", moneyFormat.formatSigned())
    }

    @Test
    fun signed_without_positive_sign() {
        val money = money("100", "EUR")
        val moneyFormat = MoneyFormat(money)
        assertEquals("EUR100", moneyFormat.formatSigned(showSignWhenPositive = false))
    }

    @Test
    fun unsigned() {
        val money = money("100", "EUR")
        val moneyFormat = MoneyFormat(money)
        assertEquals("EUR100", moneyFormat.formatUnsigned())
    }

    @Test
    fun unsigned_negative() {
        val money = money("-100", "EUR")
        val moneyFormat = MoneyFormat(money)
        assertEquals("EUR100", moneyFormat.formatUnsigned())
    }

    @Test
    fun signed_negative() {
        val money = money("-100", "EUR")
        val moneyFormat = MoneyFormat(money)
        assertEquals("- EUR100", moneyFormat.formatSigned())
    }
}