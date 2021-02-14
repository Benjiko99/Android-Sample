package com.spiraclesoftware.androidsample.domain.interactor

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.spiraclesoftware.androidsample.domain.LocalDataSource
import com.spiraclesoftware.androidsample.domain.entity.*
import com.spiraclesoftware.androidsample.domain.epochDateTime
import com.spiraclesoftware.androidsample.domain.service.CurrencyConverter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class AccountsInteractorTest {

    companion object {
        private val MOCK_ACCOUNT = Account(Currency.getInstance("EUR"))
    }

    @Mock
    private lateinit var localDataSource: LocalDataSource

    @Mock
    private lateinit var conversionRatesInteractor: ConversionRatesInteractor

    private lateinit var currencyConverter: CurrencyConverter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        currencyConverter = CurrencyConverter(conversionRatesInteractor)

        whenever(localDataSource.getAccount()) doReturn MOCK_ACCOUNT
    }

    @Test
    fun `Account is retrieved from cache`() = runBlockingTest {
        val interactor = AccountsInteractor(localDataSource, mock())
        assertEquals(MOCK_ACCOUNT, interactor.getAccount())
    }

    @Test
    fun `Transactions contribute to account balance`() = runBlockingTest {
        val conversionRates = ConversionRates(
            baseCurrency = MOCK_ACCOUNT.currency,
            validityDate = ZonedDateTime.now(),
            rates = emptyList()
        )

        whenever(conversionRatesInteractor.getConversionRates(any())) doReturn conversionRates

        val transactions = listOf(
            Transaction(
                TransactionId("1"),
                "",
                epochDateTime,
                Money(BigDecimal("100"), MOCK_ACCOUNT.currency),
                TransferDirection.INCOMING,
                TransactionCategory.TRANSFERS,
                TransactionStatus.COMPLETED,
                TransactionStatusCode.SUCCESSFUL
            ),
            Transaction(
                TransactionId("2"),
                "",
                epochDateTime,
                Money(BigDecimal("25"), MOCK_ACCOUNT.currency),
                TransferDirection.OUTGOING,
                TransactionCategory.TRANSFERS,
                TransactionStatus.COMPLETED,
                TransactionStatusCode.SUCCESSFUL
            )
        )

        val interactor = AccountsInteractor(localDataSource, currencyConverter)
        val contribution = interactor.getContributionToBalance(transactions)

        assertEquals(Money(BigDecimal("75"), MOCK_ACCOUNT.currency), contribution)
    }

    @Test
    fun `Foreign transaction contributes to account balance in the account currency`() = runBlockingTest {
        val conversionRates = ConversionRates(
            baseCurrency = Currency.getInstance("USD"),
            validityDate = ZonedDateTime.now(),
            rates = listOf(
                ConversionRate("EUR", 0.5f)
            )
        )

        whenever(conversionRatesInteractor.getConversionRates(any())) doReturn conversionRates

        val transactions = listOf(
            Transaction(
                TransactionId("1"),
                "",
                epochDateTime,
                Money(BigDecimal("100"), Currency.getInstance("USD")),
                TransferDirection.INCOMING,
                TransactionCategory.TRANSFERS,
                TransactionStatus.COMPLETED,
                TransactionStatusCode.SUCCESSFUL
            )
        )

        val interactor = AccountsInteractor(localDataSource, currencyConverter)
        val contribution = interactor.getContributionToBalance(transactions)

        val expected = Money(BigDecimal("50.0"), MOCK_ACCOUNT.currency)
        assertEquals(expected, contribution)
    }

}
