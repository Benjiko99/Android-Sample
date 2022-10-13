package com.spiraclesoftware.androidsample.domain.interactor

import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.domain.LocalDataSource
import com.spiraclesoftware.androidsample.domain.entity.*
import com.spiraclesoftware.androidsample.domain.epochDateTime
import com.spiraclesoftware.androidsample.domain.service.CurrencyConverter
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class AccountsInteractorTest {

    companion object {
        private val MOCK_ACCOUNT = Account(Currency.getInstance("EUR"))
    }

    @MockK
    lateinit var localDataSource: LocalDataSource

    @MockK
    lateinit var conversionRatesInteractor: ConversionRatesInteractor

    @InjectMockKs
    lateinit var currencyConverter: CurrencyConverter

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        every { localDataSource.getAccount() } returns MOCK_ACCOUNT
    }

    @Test
    fun `Account is retrieved from cache`() = runBlockingTest {
        val interactor = AccountsInteractor(localDataSource, mockk())
        assertThat(interactor.getAccount()).isEqualTo(MOCK_ACCOUNT)
    }

    @Test
    fun `Transactions contribute to account balance`() = runBlockingTest {
        val conversionRates = ConversionRates(
            baseCurrency = MOCK_ACCOUNT.currency,
            validityDate = ZonedDateTime.now(),
            rates = emptyList()
        )

        coEvery { conversionRatesInteractor.getConversionRates(any()) } returns conversionRates

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

        val expected = Money(BigDecimal("75"), MOCK_ACCOUNT.currency)
        assertThat(contribution).isEqualTo(expected)
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

        coEvery { conversionRatesInteractor.getConversionRates(any()) } returns conversionRates

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
        assertThat(contribution).isEqualTo(expected)
    }

}
