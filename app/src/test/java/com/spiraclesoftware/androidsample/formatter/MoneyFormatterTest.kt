package com.spiraclesoftware.androidsample.formatter

import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.domain.entity.Transaction
import com.spiraclesoftware.androidsample.money
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

class MoneyFormatterTest {

    @Test
    fun format_incoming_transaction_contributes_to_balance() {
        val transaction = mockk<Transaction> {
            every { contributesToAccountBalance() } returns true
            every { signedMoney } returns money("100", "EUR")
        }

        val moneyFormat = MoneyFormatter()
        assertThat(moneyFormat.format(transaction)).isEqualTo("+ EUR100")
    }

    @Test
    fun format_outgoing_transaction_contributes_to_balance() {
        val transaction = mockk<Transaction> {
            every { contributesToAccountBalance() } returns true
            every { signedMoney } returns money("-100", "EUR")
        }

        val moneyFormat = MoneyFormatter()
        assertThat(moneyFormat.format(transaction)).isEqualTo("- EUR100")
    }

    @Test
    fun format_transaction_does_not_contribute_to_balance() {
        val transaction = mockk<Transaction> {
            every { contributesToAccountBalance() } returns false
            every { money } returns money("100", "EUR")
        }

        val moneyFormat = MoneyFormatter()
        assertThat(moneyFormat.format(transaction)).isEqualTo("EUR100")
    }

    @Test
    fun signed() {
        val money = money("100", "EUR")
        val moneyFormat = MoneyFormatter()
        assertThat(moneyFormat.formatSigned(money)).isEqualTo("+ EUR100")
    }

    @Test
    fun signed_decimal() {
        val money = money("12345.67", "EUR")
        val moneyFormat = MoneyFormatter()
        assertThat(moneyFormat.formatSigned(money)).isEqualTo("+ EUR12,345.67")
    }

    @Test
    fun signed_decimal_with_zeros() {
        val money = money("100.00", "EUR")
        val moneyFormat = MoneyFormatter()
        assertThat(moneyFormat.formatSigned(money)).isEqualTo("+ EUR100.00")
    }

    @Test
    fun signed_without_positive_sign() {
        val money = money("100", "EUR")
        val moneyFormat = MoneyFormatter()
        assertThat(moneyFormat.formatSigned(money, showSignWhenPositive = false)).isEqualTo("EUR100")
    }

    @Test
    fun unsigned() {
        val money = money("100", "EUR")
        val moneyFormat = MoneyFormatter()
        assertThat(moneyFormat.formatUnsigned(money)).isEqualTo("EUR100")
    }

    @Test
    fun unsigned_negative() {
        val money = money("-100", "EUR")
        val moneyFormat = MoneyFormatter()
        assertThat(moneyFormat.formatUnsigned(money)).isEqualTo("EUR100")
    }

    @Test
    fun signed_negative() {
        val money = money("-100", "EUR")
        val moneyFormat = MoneyFormatter()
        assertThat(moneyFormat.formatSigned(money)).isEqualTo("- EUR100")
    }
}