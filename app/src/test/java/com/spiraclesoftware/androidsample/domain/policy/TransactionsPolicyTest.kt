package com.spiraclesoftware.androidsample.domain.policy

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.spiraclesoftware.androidsample.TestData
import com.spiraclesoftware.androidsample.domain.interactor.ConversionRatesInteractor
import com.spiraclesoftware.androidsample.domain.model.*
import com.spiraclesoftware.androidsample.money
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Test
import java.math.BigDecimal
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionsPolicyTest {

    @Test
    fun `Successful transactions contribute to account balance`() {
        val transaction = Transaction(
            TransactionId(1),
            "",
            TestData.epochDateTime,
            money("100", "EUR"),
            TransferDirection.INCOMING,
            TransactionCategory.TRANSFERS,
            TransactionStatus.COMPLETED,
            TransactionStatusCode.SUCCESSFUL
        )

        assertTrue(TransactionsPolicy.contributesToBalance(transaction))
    }

    @Test
    fun `Declined transactions do not contribute to account balance`() {
        val transaction = Transaction(
            TransactionId(1),
            "",
            TestData.epochDateTime,
            money("100", "EUR"),
            TransferDirection.INCOMING,
            TransactionCategory.TRANSFERS,
            TransactionStatus.DECLINED,
            TransactionStatusCode.SPENDING_LIMIT_EXCEEDED
        )

        assertFalse(TransactionsPolicy.contributesToBalance(transaction))
    }

    @Test
    fun `Transaction in account currency contributes its positive amount to account balance`() = runBlockingTest {
        val accountCurrency = TestData.account.currency

        val transaction = Transaction(
            TransactionId(1),
            "",
            TestData.epochDateTime,
            Money(BigDecimal("100"), accountCurrency),
            TransferDirection.INCOMING,
            TransactionCategory.TRANSFERS,
            TransactionStatus.COMPLETED,
            TransactionStatusCode.SUCCESSFUL
        )

        val conversionRates = ConversionRates(
            baseCurrency = accountCurrency,
            validityDate = Date(),
            rates = emptyList()
        )

        val conversionRatesInteractor = mock<ConversionRatesInteractor>()
        whenever(conversionRatesInteractor.getConversionRates(any())) doReturn conversionRates
        val currencyConverter = CurrencyConverter(conversionRatesInteractor)
        val transactionsPolicy = TransactionsPolicy(currencyConverter)

        val contribution = transactionsPolicy.getContributionToBalance(transaction, accountCurrency)

        assertEquals(Money(BigDecimal("100"), accountCurrency), contribution)
    }

    @Test
    fun `Transaction in account currency contributes its negative amount to account balance`() = runBlockingTest {
        val accountCurrency = TestData.account.currency

        val transaction = Transaction(
            TransactionId(1),
            "",
            TestData.epochDateTime,
            Money(BigDecimal("100"), accountCurrency),
            TransferDirection.OUTGOING,
            TransactionCategory.TRANSFERS,
            TransactionStatus.COMPLETED,
            TransactionStatusCode.SUCCESSFUL
        )

        val conversionRates = ConversionRates(
            baseCurrency = accountCurrency,
            validityDate = Date(),
            rates = emptyList()
        )

        val conversionRatesInteractor = mock<ConversionRatesInteractor>()
        whenever(conversionRatesInteractor.getConversionRates(any())) doReturn conversionRates
        val currencyConverter = CurrencyConverter(conversionRatesInteractor)
        val transactionsPolicy = TransactionsPolicy(currencyConverter)

        val contribution = transactionsPolicy.getContributionToBalance(transaction, accountCurrency)

        assertEquals(Money(BigDecimal("-100"), accountCurrency), contribution)
    }

    @Test
    fun `Transaction in foreign currency contributes its amount in account currency to balance`() = runBlockingTest {
        val accountCurrency = TestData.account.currency

        val transaction = Transaction(
            TransactionId(1),
            "",
            TestData.epochDateTime,
            Money(BigDecimal("100"), Currency.getInstance("USD")),
            TransferDirection.INCOMING,
            TransactionCategory.TRANSFERS,
            TransactionStatus.COMPLETED,
            TransactionStatusCode.SUCCESSFUL
        )

        val conversionRates = ConversionRates(
            baseCurrency = Currency.getInstance("USD"),
            validityDate = Date(),
            rates = listOf(
                ConversionRate(accountCurrency, 0.5f)
            )
        )

        val conversionRatesInteractor = mock<ConversionRatesInteractor>()
        whenever(conversionRatesInteractor.getConversionRates(any())) doReturn conversionRates
        val currencyConverter = CurrencyConverter(conversionRatesInteractor)
        val transactionsPolicy = TransactionsPolicy(currencyConverter)

        val contribution = transactionsPolicy.getContributionToBalance(transaction, accountCurrency)

        val money = Money(BigDecimal("100"), Currency.getInstance("USD"))
        val expectedContribution = currencyConverter.convert(money, accountCurrency)
        assertEquals(expectedContribution, contribution)
    }

}