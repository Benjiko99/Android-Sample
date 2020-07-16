package com.spiraclesoftware.androidsample.domain.interactor

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.spiraclesoftware.androidsample.TestData
import com.spiraclesoftware.androidsample.data.memory.MemoryDataSource
import com.spiraclesoftware.androidsample.data.network.NetworkDataSource
import com.spiraclesoftware.androidsample.domain.model.currencyCode
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.internal.verification.Times

@OptIn(ExperimentalCoroutinesApi::class)
class ConversionRatesInteractorTest {

    companion object {
        private val MOCK_BASE_CURRENCY = TestData.account.currency.currencyCode()
        private val MOCK_RATES = TestData.conversionRates
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
        whenever(networkDataSource.getConversionRates(any())) doReturn MOCK_RATES

        val interactor = ConversionRatesInteractor(networkDataSource, memoryDataSource)

        val conversionRates = interactor.getConversionRates(MOCK_BASE_CURRENCY)
        assertEquals(MOCK_RATES, conversionRates)
    }

    @Test
    fun `Rates are loaded from network when ignoring cached`() = runBlockingTest {
        whenever(memoryDataSource.getConversionRates(any())) doReturn null
        whenever(networkDataSource.getConversionRates(any())) doReturn MOCK_RATES

        val interactor = ConversionRatesInteractor(networkDataSource, memoryDataSource)

        interactor.getConversionRates(MOCK_BASE_CURRENCY, ignoreCache = true)

        verify(networkDataSource).getConversionRates(MOCK_BASE_CURRENCY)
        verify(memoryDataSource, Times(0)).getConversionRates(any())
    }

    @Test
    fun `Rates are saved on disk when loaded from network`() = runBlockingTest {
        whenever(memoryDataSource.getConversionRates(any())) doReturn null
        whenever(networkDataSource.getConversionRates(any())) doReturn MOCK_RATES

        val interactor = ConversionRatesInteractor(networkDataSource, memoryDataSource)

        interactor.getConversionRates(MOCK_BASE_CURRENCY)

        verify(memoryDataSource).saveConversionRates(
            MOCK_BASE_CURRENCY,
            MOCK_RATES
        )
    }

}
