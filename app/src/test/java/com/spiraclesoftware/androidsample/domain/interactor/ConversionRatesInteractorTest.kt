package com.spiraclesoftware.androidsample.domain.interactor

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.spiraclesoftware.androidsample.TestData
import com.spiraclesoftware.androidsample.data.disk.DiskDataSource
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
    private lateinit var diskDataSource: DiskDataSource

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `Rates are loaded correctly from disk`() = runBlockingTest {
        whenever(diskDataSource.getConversionRates(any())) doReturn MOCK_RATES

        val interactor =
            ConversionRatesInteractor(
                networkDataSource,
                diskDataSource
            )

        val conversionRates = interactor.getConversionRates(MOCK_BASE_CURRENCY)
        assertEquals(MOCK_RATES, conversionRates)
    }

    @Test
    fun `Rates are loaded correctly from network`() = runBlockingTest {
        whenever(diskDataSource.getConversionRates(any())) doReturn null
        whenever(networkDataSource.getConversionRates(any())) doReturn MOCK_RATES

        val interactor =
            ConversionRatesInteractor(
                networkDataSource,
                diskDataSource
            )

        val conversionRates = interactor.getConversionRates(MOCK_BASE_CURRENCY)
        assertEquals(MOCK_RATES, conversionRates)
    }

    @Test
    fun `Rates are loaded from network when ignoring cached`() = runBlockingTest {
        whenever(diskDataSource.getConversionRates(any())) doReturn null
        whenever(networkDataSource.getConversionRates(any())) doReturn MOCK_RATES

        val interactor =
            ConversionRatesInteractor(
                networkDataSource,
                diskDataSource
            )

        interactor.getConversionRates(MOCK_BASE_CURRENCY, ignoreCached = true)

        verify(networkDataSource).getConversionRates(MOCK_BASE_CURRENCY)
        verify(diskDataSource, Times(0)).getConversionRates(any())
    }

    @Test
    fun `Rates are saved on disk when loaded from network`() = runBlockingTest {
        whenever(diskDataSource.getConversionRates(any())) doReturn null
        whenever(networkDataSource.getConversionRates(any())) doReturn MOCK_RATES

        val interactor =
            ConversionRatesInteractor(
                networkDataSource,
                diskDataSource
            )

        interactor.getConversionRates(MOCK_BASE_CURRENCY)

        verify(diskDataSource).saveConversionRates(
            MOCK_BASE_CURRENCY,
            MOCK_RATES
        )
    }

}
