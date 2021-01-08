package com.spiraclesoftware.androidsample.domain.interactor

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.spiraclesoftware.androidsample.domain.MemoryDataSource
import com.spiraclesoftware.androidsample.domain.NetworkDataSource
import com.spiraclesoftware.androidsample.domain.model.ConversionRate
import com.spiraclesoftware.androidsample.domain.model.ConversionRates
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.threeten.bp.ZonedDateTime
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class ConversionRatesInteractorTest {

    companion object {
        private val MOCK_BASE_CURRENCY = Currency.getInstance("EUR")
        private val MOCK_RATES = ConversionRates(
            baseCurrency = Currency.getInstance("EUR"),
            validityDate = ZonedDateTime.now(),
            rates = listOf(
                ConversionRate("USD", 1.12f),
                ConversionRate("CZK", 26.70f)
            )
        )
    }

    @Mock
    private lateinit var networkDataSource: NetworkDataSource

    @Mock
    private lateinit var memoryDataSource: MemoryDataSource

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `Rates are loaded correctly from cache`() = runBlockingTest {
        whenever(memoryDataSource.getConversionRates(any())) doReturn MOCK_RATES

        val interactor = ConversionRatesInteractor(networkDataSource, memoryDataSource)

        val conversionRates = interactor.getConversionRates(MOCK_BASE_CURRENCY)
        assertEquals(MOCK_RATES, conversionRates)
    }

    @Test
    fun `Rates are loaded correctly from network`() = runBlockingTest {
        whenever(memoryDataSource.getConversionRates(any())) doReturn null
        whenever(networkDataSource.fetchConversionRates(any())) doReturn MOCK_RATES

        val interactor = ConversionRatesInteractor(networkDataSource, memoryDataSource)

        val conversionRates = interactor.getConversionRates(MOCK_BASE_CURRENCY)
        assertEquals(MOCK_RATES, conversionRates)
    }

    @Test
    fun `Rates are loaded from network if cache is empty`() = runBlockingTest {
        whenever(memoryDataSource.getConversionRates(any())) doReturn null
        whenever(networkDataSource.fetchConversionRates(any())) doReturn MOCK_RATES

        val interactor = ConversionRatesInteractor(networkDataSource, memoryDataSource)

        interactor.getConversionRates(MOCK_BASE_CURRENCY)

        verify(networkDataSource).fetchConversionRates(MOCK_BASE_CURRENCY)
    }

    @Test
    fun `Rates are correctly fetched from network`() = runBlockingTest {
        whenever(networkDataSource.fetchConversionRates(any())) doReturn MOCK_RATES

        val interactor = ConversionRatesInteractor(networkDataSource, memoryDataSource)

        val rates = interactor.fetchConversionRates(MOCK_BASE_CURRENCY)
        assertEquals(MOCK_RATES, rates)

        verify(networkDataSource).fetchConversionRates(MOCK_BASE_CURRENCY)
    }

    @Test
    fun `Rates are saved on disk when fetched from network`() = runBlockingTest {
        whenever(memoryDataSource.getConversionRates(any())) doReturn null
        whenever(networkDataSource.fetchConversionRates(any())) doReturn MOCK_RATES

        val interactor = ConversionRatesInteractor(networkDataSource, memoryDataSource)

        interactor.getConversionRates(MOCK_BASE_CURRENCY)

        verify(memoryDataSource).saveConversionRates(MOCK_BASE_CURRENCY, MOCK_RATES)
    }

}
