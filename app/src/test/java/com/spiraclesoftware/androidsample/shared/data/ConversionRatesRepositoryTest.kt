package com.spiraclesoftware.androidsample.shared.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.stub
import com.spiraclesoftware.androidsample.TestData
import com.spiraclesoftware.androidsample.TestData.BASE_CURRENCY_CODE
import com.spiraclesoftware.androidsample.application.data.RevolutApiService
import com.spiraclesoftware.androidsample.shared.domain.ConversionRates
import com.spiraclesoftware.androidsample.shared.domain.CurrencyCode
import com.spiraclesoftware.androidsample.utils.LiveDataTestUtil
import com.spiraclesoftware.core.data.AssociatedItemCache
import com.spiraclesoftware.core.data.InstantAppExecutors
import com.spiraclesoftware.core.data.Status
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ConversionRatesRepositoryTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var apiService: RevolutApiService

    @Mock
    private lateinit var cache: AssociatedItemCache<CurrencyCode, ConversionRates>

    private lateinit var conversionRatesRepository: ConversionRatesRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        conversionRatesRepository =
            ConversionRatesRepository(InstantAppExecutors(), apiService, cache)
    }

    private fun stubConversionRatesCache(single: ConversionRates? = null) {
        cache.stub {
            on { get(any()) }.doReturn(single)
        }
    }

    @Test
    fun `Given that rates are cached, return cached rates`() {
        stubConversionRatesCache(TestData.conversionRates)

        val resourceLD = conversionRatesRepository.getConversionRates(BASE_CURRENCY_CODE)
        val resource = LiveDataTestUtil.getValue(resourceLD)

        assertEquals(Status.SUCCESS, resource?.status)
        assertEquals(TestData.conversionRates, resource?.data)
    }
}