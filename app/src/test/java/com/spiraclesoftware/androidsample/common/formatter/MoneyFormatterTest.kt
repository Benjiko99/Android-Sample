package com.spiraclesoftware.androidsample.common.formatter

import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.domain.entity.Money
import com.spiraclesoftware.androidsample.domain.entity.Transaction
import com.spiraclesoftware.androidsample.framework.utils.LanguageManager.AppLanguage
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import java.text.NumberFormat
import java.util.*

class MoneyFormatterTest {

    @OptIn(ExperimentalStdlibApi::class)
    companion object {
        private val NON_BREAKING_SPACE = Char(160)
        private val SPACE = Char(32)
    }

    /**
     * When compiling outside Android Studio (e.g. on a build server),
     * the [NumberFormat] uses a different character for spaces for some
     * reason. This breaks unit tests, so we're gonna replace it with
     * regular spaces each time.
     *
     * A non-breaking space is a space character that prevents an automatic
     * line break at its position.
     */
    private fun String.unifyEncoding(): String {
        return replace(NON_BREAKING_SPACE, SPACE)
    }

    @Before
    fun setUp() {
        Locale.setDefault(AppLanguage.ENGLISH.toLocale())
    }

    @Test
    fun localization_english() {
        Locale.setDefault(AppLanguage.ENGLISH.toLocale())

        val money = Money("100", "CZK")

        val actual = MoneyFormatter().formatUnsigned(money)
        assertThat(actual).isEqualTo("CZK100")
    }

    @Test
    fun localization_czech() {
        Locale.setDefault(AppLanguage.CZECH.toLocale())

        val money = Money("100", "CZK")

        val actual = MoneyFormatter().formatUnsigned(money).unifyEncoding()
        assertThat(actual).isEqualTo("100 Kč")
    }

    @Test
    fun format_incoming_transaction_contributes_to_balance() {
        val transaction = mockk<Transaction> {
            every { contributesToAccountBalance() } returns true
            every { signedMoney } returns Money("100", "USD")
        }

        val actual = MoneyFormatter().format(transaction)
        assertThat(actual).isEqualTo("+ $100")
    }

    @Test
    fun format_outgoing_transaction_contributes_to_balance() {
        val transaction = mockk<Transaction> {
            every { contributesToAccountBalance() } returns true
            every { signedMoney } returns Money("-100", "USD")
        }

        val actual = MoneyFormatter().format(transaction)
        assertThat(actual).isEqualTo("- $100")
    }

    @Test
    fun format_transaction_does_not_contribute_to_balance() {
        val transaction = mockk<Transaction> {
            every { contributesToAccountBalance() } returns false
            every { money } returns Money("100", "USD")
        }

        val actual = MoneyFormatter().format(transaction)
        assertThat(actual).isEqualTo("$100")
    }

    @Test
    fun signed() {
        val money = Money("100", "USD")
        val actual = MoneyFormatter().formatSigned(money)
        assertThat(actual).isEqualTo("+ $100")
    }

    @Test
    fun signed_decimal() {
        val money = Money("12345.67", "USD")
        val actual = MoneyFormatter().formatSigned(money)
        assertThat(actual).isEqualTo("+ $12,345.67")
    }

    @Test
    fun signed_decimal_with_zeros() {
        val money = Money("100.00", "USD")
        val actual = MoneyFormatter().formatSigned(money)
        assertThat(actual).isEqualTo("+ $100")
    }

    @Test
    fun signed_without_positive_sign() {
        val money = Money("100", "USD")
        val actual = MoneyFormatter().formatSigned(money, showSignWhenPositive = false)
        assertThat(actual).isEqualTo("$100")
    }

    @Test
    fun unsigned() {
        val money = Money("100", "USD")

        val actual = MoneyFormatter().formatUnsigned(money)
        assertThat(actual).isEqualTo("$100")
    }

    @Test
    fun unsigned_negative() {
        val money = Money("-100", "USD")
        val actual = MoneyFormatter().formatUnsigned(money)
        assertThat(actual).isEqualTo("$100")
    }

    @Test
    fun signed_negative() {
        val money = Money("-100", "USD")
        val actual = MoneyFormatter().formatSigned(money)
        assertThat(actual).isEqualTo("- $100")
    }

}
