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

    @Test
    fun addition() {
        fun case1() {
            val first = Money("1", "EUR")
            val second = Money("99", "EUR")

            val sum = first.add(second.amount).amount
            assertEquals(BigDecimal("100"), sum)
        }
        case1()

        fun case2() {
            val first = Money("100000", "EUR")
            val second = Money("123456", "EUR")

            val sum = first.add(second.amount).amount
            assertEquals(BigDecimal("223456"), sum)
        }
        case2()

        fun case3() {
            val first = Money("1234.567", "EUR")
            val second = Money("1.234", "EUR")

            val sum = first.add(second.amount).amount
            assertEquals(BigDecimal("1235.801"), sum)
        }
        case3()
    }

    @Test
    fun subtraction() {
        fun case1() {
            val first = Money("100", "EUR")
            val second = Money("99", "EUR")

            val sum = first.subtract(second.amount).amount
            assertEquals(BigDecimal("1"), sum)
        }
        case1()

        fun case2() {
            val first = Money("123456", "EUR")
            val second = Money("100000", "EUR")

            val sum = first.subtract(second.amount).amount
            assertEquals(BigDecimal("23456"), sum)
        }
        case2()

        fun case3() {
            val first = Money("1234.567", "EUR")
            val second = Money("1.234", "EUR")

            val sum = first.subtract(second.amount).amount
            assertEquals(BigDecimal("1233.333"), sum)
        }
        case3()
    }

    @Test
    fun multiplication() {
        fun case1() {
            val first = Money("100", "EUR")
            val second = Money("42", "EUR")

            val sum = first.multiply(second.amount).amount
            assertEquals(BigDecimal("4200"), sum)
        }
        case1()

        fun case2() {
            val first = Money("100", "EUR")
            val second = Money("123.456", "EUR")

            val sum = first.multiply(second.amount).amount
            assertEquals(BigDecimal("12345.60"), sum)
        }
        case2()
    }

    @Test
    fun division() {
        fun case1() {
            val first = Money("100", "EUR")
            val second = Money("50", "EUR")

            val sum = first.divide(second.amount).amount
            assertEquals(BigDecimal("2"), sum)
        }
        case1()

        fun case2() {
            val first = Money("100", "EUR")
            val second = Money("3", "EUR")

            val sum = first.divide(second.amount).amount
            assertEquals(BigDecimal("33.33333"), sum)
        }
        case2()
    }

    @Test
    fun formatting() {
        fun case_signed() {
            val money = Money("100", "EUR")
            assertEquals("+ EUR100", money.formatSigned())
        }
        case_signed()

        fun case_signed_decimal() {
            val money = Money("12345.67", "EUR")
            assertEquals("+ EUR12,345.67", money.formatSigned())
        }
        case_signed_decimal()

        fun case_signed_decimal_zeros() {
            val money = Money("100.00", "EUR")
            assertEquals("+ EUR100.00", money.formatSigned())
        }
        case_signed_decimal_zeros()

        fun case_signed_without_positive_sign() {
            val money = Money("100", "EUR")
            assertEquals("EUR100", money.formatSigned(showSignWhenPositive = false))
        }
        case_signed_without_positive_sign()

        fun case_unsigned() {
            val money = Money("100", "EUR")
            assertEquals("EUR100", money.formatUnsigned())
        }
        case_unsigned()

        fun case_unsigned_negative() {
            val money = Money("-100", "EUR")
            assertEquals("EUR100", money.formatUnsigned())
        }
        case_unsigned_negative()

        fun case_signed_negative() {
            val money = Money("-100", "EUR")
            assertEquals("- EUR100", money.formatSigned())
        }
        case_signed_negative()
    }
}