package com.spiraclesoftware.androidsample.domain.policy

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import com.spiraclesoftware.androidsample.domain.interactor.ConversionRatesInteractor
import com.spiraclesoftware.androidsample.domain.model.ConversionRate
import com.spiraclesoftware.androidsample.domain.model.ConversionRates
import com.spiraclesoftware.androidsample.domain.money
import com.spiraclesoftware.androidsample.domain.policy.CurrencyConverter.MissingConversionRateException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.threeten.bp.ZonedDateTime
import java.math.BigDecimal
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class CurrencyConverterTest {

    @Mock
    private lateinit var conversionRatesInteractor: ConversionRatesInteractor

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `Convert to foreign currency correctly`() = runBlockingTest {
        val conversionRates = ConversionRates(
            baseCurrency = Currency.getInstance("USD"),
            validityDate = ZonedDateTime.now(),
            rates = listOf(ConversionRate("CZK", 2f))
        )

        whenever(conversionRatesInteractor.getConversionRates(any())) doReturn conversionRates

        val currencyConverter = CurrencyConverter(conversionRatesInteractor)

        val usd = money("1", "USD")

        val newCurrency = Currency.getInstance("CZK")
        val usdToCzk = currencyConverter.convert(usd, newCurrency)

        assert(usdToCzk.amountEquals(BigDecimal("2")))
        assertEquals(newCurrency, usdToCzk.currency)
    }

    @Test
    fun `Converting to the same currency returns original money`() = runBlockingTest {
        val conversionRates = ConversionRates(
            baseCurrency = Currency.getInstance("EUR"),
            validityDate = ZonedDateTime.now(),
            rates = emptyList()
        )

        whenever(conversionRatesInteractor.getConversionRates(any())) doReturn conversionRates

        val currencyConverter = CurrencyConverter(conversionRatesInteractor)

        val eur = money("100", "EUR")

        val newCurrency = Currency.getInstance("EUR")
        val eurToEur = currencyConverter.convert(eur, newCurrency)

        assertEquals(eur, eurToEur)
    }

    @Test
    fun `Throw exception when missing a conversion rate for given currency`() = runBlockingTest {
        val conversionRates = ConversionRates(
            baseCurrency = Currency.getInstance("USD"),
            validityDate = ZonedDateTime.now(),
            rates = emptyList()
        )

        whenever(conversionRatesInteractor.getConversionRates(any())) doReturn conversionRates

        val currencyConverter = CurrencyConverter(conversionRatesInteractor)

        val usd = money("100", "USD")

        assertThrows(MissingConversionRateException::class.java) {
            runBlocking {
                currencyConverter.convert(usd, Currency.getInstance("JPY"))
            }
        }
    }

}