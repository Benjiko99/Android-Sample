package com.spiraclesoftware.androidsample.shared.ui

import com.spiraclesoftware.androidsample.shared.domain.Money
import junit.framework.Assert.assertEquals
import org.junit.Test

class MoneyFormatTest {

    @Test
    fun signed() {
        val money = Money("100", "EUR")
        assertEquals("+ EUR100", money.formatSigned())
    }

    @Test
    fun signed_decimal() {
        val money = Money("12345.67", "EUR")
        assertEquals("+ EUR12,345.67", money.formatSigned())
    }

    @Test
    fun signed_decimal_with_zeros() {
        val money = Money("100.00", "EUR")
        assertEquals("+ EUR100.00", money.formatSigned())
    }

    @Test
    fun signed_without_positive_sign() {
        val money = Money("100", "EUR")
        assertEquals("EUR100", money.formatSigned(showSignWhenPositive = false))
    }

    @Test
    fun unsigned() {
        val money = Money("100", "EUR")
        assertEquals("EUR100", money.formatUnsigned())
    }

    @Test
    fun unsigned_negative() {
        val money = Money("-100", "EUR")
        assertEquals("EUR100", money.formatUnsigned())
    }

    @Test
    fun signed_negative() {
        val money = Money("-100", "EUR")
        assertEquals("- EUR100", money.formatSigned())
    }
}