package com.spiraclesoftware.androidsample.features.rates.converter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.MutableLiveData
import com.spiraclesoftware.androidsample.application.extensions.findByCurrency
import com.spiraclesoftware.androidsample.shared.domain.ConversionRate
import com.spiraclesoftware.androidsample.shared.domain.ConversionRates
import com.spiraclesoftware.androidsample.utils.LiveDataTestUtil
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import java.util.*

class RatesConverterTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var ratesConverter: RatesConverter

    companion object {
        private val EUR = Currency.getInstance("EUR")
        private val CZK = Currency.getInstance("CZK")
        private val USD = Currency.getInstance("USD")
    }

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        val lifecycle = LifecycleRegistry(mock(LifecycleOwner::class.java))
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        ratesConverter = RatesConverter(lifecycle).apply {
            setConversionRates(
                MutableLiveData<ConversionRates>().apply {
                    value = ConversionRates(
                        baseCurrency = EUR,
                        validityDate = Date(),
                        rates = listOf(
                            ConversionRate(EUR, 1.0f),
                            ConversionRate(CZK, 25.0f),
                            ConversionRate(USD, 1.1f)
                        )
                    )
                }
            )
        }
    }

    @Test
    fun `Given no interaction then rates are unchanged`() {
        val rates = LiveDataTestUtil
            .getValue(ratesConverter.adjustedConversionRates)!!.rates

        assertEquals(25.0f, rates.findByCurrency("CZK")!!.rate)
        assertEquals(1.0f, rates.findByCurrency("EUR")!!.rate)
        assertEquals(1.1f, rates.findByCurrency("USD")!!.rate)
    }

    @Test
    fun `Given value of base changes indirectly all rates are adjusted correctly`() {
        ratesConverter.adjustValueOfBase(CZK, 1.0f)

        var rates = LiveDataTestUtil
            .getValue(ratesConverter.adjustedConversionRates)!!.rates

        assertEquals(1.0f, rates.findByCurrency("CZK")!!.rate)
        assertEquals(0.04f, rates.findByCurrency("EUR")!!.rate)
        assertEquals(0.044f, rates.findByCurrency("USD")!!.rate)

        // SECOND UPDATE

        ratesConverter.adjustValueOfBase(CZK, 25.0f)

        rates = LiveDataTestUtil
            .getValue(ratesConverter.adjustedConversionRates)!!.rates

        assertEquals(25.0f, rates.findByCurrency("CZK")!!.rate)
        assertEquals(1.0f, rates.findByCurrency("EUR")!!.rate)
        assertEquals(1.1f, rates.findByCurrency("USD")!!.rate)
    }
}