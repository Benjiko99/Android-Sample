package com.spiraclesoftware.androidsample.domain.service

import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.domain.entity.ConversionRate
import com.spiraclesoftware.androidsample.domain.entity.ConversionRates
import com.spiraclesoftware.androidsample.domain.interactor.ConversionRatesInteractor
import com.spiraclesoftware.androidsample.domain.money
import com.spiraclesoftware.androidsample.domain.service.CurrencyConverter.MissingConversionRateException
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class CurrencyConverterTest {

    @MockK
    lateinit var conversionRatesInteractor: ConversionRatesInteractor

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `Convert to foreign currency correctly`() = runBlockingTest {
        val conversionRates = ConversionRates(
            baseCurrency = Currency.getInstance("USD"),
            validityDate = ZonedDateTime.now(),
            rates = listOf(ConversionRate("CZK", 2f))
        )

        coEvery { conversionRatesInteractor.getConversionRates(any()) } returns conversionRates

        val currencyConverter = CurrencyConverter(conversionRatesInteractor)

        val usd = money("1", "USD")

        val newCurrency = Currency.getInstance("CZK")
        val usdToCzk = currencyConverter.convert(usd, newCurrency)

        assertThat(usdToCzk.amountEquals(BigDecimal("2"))).isTrue()
        assertThat(usdToCzk.currency).isEqualTo(newCurrency)
    }

    @Test
    fun `Converting to the same currency returns original money`() = runBlockingTest {
        val conversionRates = ConversionRates(
            baseCurrency = Currency.getInstance("EUR"),
            validityDate = ZonedDateTime.now(),
            rates = emptyList()
        )

        coEvery { conversionRatesInteractor.getConversionRates(any()) } returns conversionRates

        val currencyConverter = CurrencyConverter(conversionRatesInteractor)

        val eur = money("100", "EUR")

        val newCurrency = Currency.getInstance("EUR")
        val eurToEur = currencyConverter.convert(eur, newCurrency)

        assertThat(eurToEur).isEqualTo(eur)
    }

    @Test
    fun `Throw exception when missing a conversion rate for given currency`() = runBlockingTest {
        val conversionRates = ConversionRates(
            baseCurrency = Currency.getInstance("USD"),
            validityDate = ZonedDateTime.now(),
            rates = emptyList()
        )

        coEvery { conversionRatesInteractor.getConversionRates(any()) } returns conversionRates

        val currencyConverter = CurrencyConverter(conversionRatesInteractor)

        val usd = money("100", "USD")

        assertThrows(MissingConversionRateException::class.java) {
            runBlocking {
                currencyConverter.convert(usd, Currency.getInstance("JPY"))
            }
        }
    }

}