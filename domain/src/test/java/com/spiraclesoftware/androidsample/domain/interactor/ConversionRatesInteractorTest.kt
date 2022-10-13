package com.spiraclesoftware.androidsample.domain.interactor

import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.domain.LocalDataSource
import com.spiraclesoftware.androidsample.domain.RemoteDataSource
import com.spiraclesoftware.androidsample.domain.entity.ConversionRate
import com.spiraclesoftware.androidsample.domain.entity.ConversionRates
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.ZonedDateTime
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

    @MockK
    private lateinit var remoteDataSource: RemoteDataSource

    @MockK
    private lateinit var localDataSource: LocalDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `Rates are loaded correctly from cache`() = runBlockingTest {
        every { localDataSource.getConversionRates(any()) } returns MOCK_RATES

        val interactor = ConversionRatesInteractor(remoteDataSource, localDataSource)

        val conversionRates = interactor.getConversionRates(MOCK_BASE_CURRENCY)
        assertThat(conversionRates).isEqualTo(MOCK_RATES)
    }

    @Test
    fun `Rates are loaded correctly from remote`() = runBlockingTest {
        every { localDataSource.getConversionRates(any()) } returns null
        coEvery { remoteDataSource.fetchConversionRates(any()) } returns MOCK_RATES
        coJustRun { localDataSource.saveConversionRates(any(), any()) }

        val interactor = ConversionRatesInteractor(remoteDataSource, localDataSource)

        val conversionRates = interactor.getConversionRates(MOCK_BASE_CURRENCY)
        assertThat(conversionRates).isEqualTo(MOCK_RATES)
    }

    @Test
    fun `Rates are loaded from remote if cache is empty`() = runBlockingTest {
        every { localDataSource.getConversionRates(any()) } returns null
        coEvery { remoteDataSource.fetchConversionRates(any()) } returns MOCK_RATES
        coJustRun { localDataSource.saveConversionRates(any(), any()) }

        val interactor = ConversionRatesInteractor(remoteDataSource, localDataSource)

        interactor.getConversionRates(MOCK_BASE_CURRENCY)

        coVerify { remoteDataSource.fetchConversionRates(MOCK_BASE_CURRENCY) }
    }

    @Test
    fun `Rates are correctly fetched from remote`() = runBlockingTest {
        coEvery { remoteDataSource.fetchConversionRates(any()) } returns MOCK_RATES
        coJustRun { localDataSource.saveConversionRates(any(), any()) }

        val interactor = ConversionRatesInteractor(remoteDataSource, localDataSource)

        val rates = interactor.fetchConversionRates(MOCK_BASE_CURRENCY)
        assertEquals(MOCK_RATES, rates)

        coVerify { remoteDataSource.fetchConversionRates(MOCK_BASE_CURRENCY) }
    }

    @Test
    fun `Rates are saved on local when fetched from remote`() = runBlockingTest {
        every { localDataSource.getConversionRates(any()) } returns null
        coEvery { remoteDataSource.fetchConversionRates(any()) } returns MOCK_RATES
        coJustRun { localDataSource.saveConversionRates(any(), any()) }

        val interactor = ConversionRatesInteractor(remoteDataSource, localDataSource)

        interactor.getConversionRates(MOCK_BASE_CURRENCY)

        coVerify { localDataSource.saveConversionRates(MOCK_BASE_CURRENCY, MOCK_RATES) }
    }

}
