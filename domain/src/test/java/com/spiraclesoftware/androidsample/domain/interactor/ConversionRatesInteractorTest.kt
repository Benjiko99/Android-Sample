package com.spiraclesoftware.androidsample.domain.interactor

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.spiraclesoftware.androidsample.domain.LocalDataSource
import com.spiraclesoftware.androidsample.domain.RemoteDataSource
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
    private lateinit var remoteDataSource: RemoteDataSource

    @Mock
    private lateinit var localDataSource: LocalDataSource

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `Rates are loaded correctly from cache`() = runBlockingTest {
        whenever(localDataSource.getConversionRates(any())) doReturn MOCK_RATES

        val interactor = ConversionRatesInteractor(remoteDataSource, localDataSource)

        val conversionRates = interactor.getConversionRates(MOCK_BASE_CURRENCY)
        assertEquals(MOCK_RATES, conversionRates)
    }

    @Test
    fun `Rates are loaded correctly from remote`() = runBlockingTest {
        whenever(localDataSource.getConversionRates(any())) doReturn null
        whenever(remoteDataSource.fetchConversionRates(any())) doReturn MOCK_RATES

        val interactor = ConversionRatesInteractor(remoteDataSource, localDataSource)

        val conversionRates = interactor.getConversionRates(MOCK_BASE_CURRENCY)
        assertEquals(MOCK_RATES, conversionRates)
    }

    @Test
    fun `Rates are loaded from remote if cache is empty`() = runBlockingTest {
        whenever(localDataSource.getConversionRates(any())) doReturn null
        whenever(remoteDataSource.fetchConversionRates(any())) doReturn MOCK_RATES

        val interactor = ConversionRatesInteractor(remoteDataSource, localDataSource)

        interactor.getConversionRates(MOCK_BASE_CURRENCY)

        verify(remoteDataSource).fetchConversionRates(MOCK_BASE_CURRENCY)
    }

    @Test
    fun `Rates are correctly fetched from remote`() = runBlockingTest {
        whenever(remoteDataSource.fetchConversionRates(any())) doReturn MOCK_RATES

        val interactor = ConversionRatesInteractor(remoteDataSource, localDataSource)

        val rates = interactor.fetchConversionRates(MOCK_BASE_CURRENCY)
        assertEquals(MOCK_RATES, rates)

        verify(remoteDataSource).fetchConversionRates(MOCK_BASE_CURRENCY)
    }

    @Test
    fun `Rates are saved on local when fetched from remote`() = runBlockingTest {
        whenever(localDataSource.getConversionRates(any())) doReturn null
        whenever(remoteDataSource.fetchConversionRates(any())) doReturn MOCK_RATES

        val interactor = ConversionRatesInteractor(remoteDataSource, localDataSource)

        interactor.getConversionRates(MOCK_BASE_CURRENCY)

        verify(localDataSource).saveConversionRates(MOCK_BASE_CURRENCY, MOCK_RATES)
    }

}
