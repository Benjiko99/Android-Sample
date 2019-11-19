package com.spiraclesoftware.androidsample.features.rates.converter

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.spiraclesoftware.androidsample.application.extensions.findByCurrency
import com.spiraclesoftware.androidsample.shared.data.ConversionRatesRepository
import com.spiraclesoftware.androidsample.shared.domain.ConversionRates
import com.spiraclesoftware.androidsample.shared.domain.CurrencyCode
import com.spiraclesoftware.core.data.Resource
import com.spiraclesoftware.core.data.Status
import com.spiraclesoftware.core.extensions.postDelayedForever
import java.util.*

class RatesConverter(
    private val ratesRepo: ConversionRatesRepository,
    private val baseCurrencyCode: CurrencyCode
) {

    private var conversionRatesResource: LiveData<Resource<ConversionRates>>? = null

    private val _adjustedConversionRates = MutableLiveData<Resource<ConversionRates>>()
    val adjustedConversionRates: LiveData<Resource<ConversionRates>> get() = _adjustedConversionRates

    init {
        setupAutoRefreshOfConversionRates()
    }

    private fun setupAutoRefreshOfConversionRates() {
        fun refreshConversionRates() {
            ratesRepo.dirtyCache()
            ratesRepo.getConversionRates(baseCurrencyCode).also {
                setConversionRates(it)
            }
        }

        Handler(Looper.getMainLooper()).postDelayedForever(
            // TODO: Extract delay to companion or somewhere
            delayMillis = 5000L,
            delayFirstRun = false,
            function = ::refreshConversionRates
        )
    }

    private fun setConversionRates(conversionRatesResource: LiveData<Resource<ConversionRates>>) {
        this.conversionRatesResource = conversionRatesResource

        // TODO: There's no way to stop observing because I have no callback for when
        //  this class is destroyed
        val observer = Observer<Resource<ConversionRates>> {
            if (it.status == Status.SUCCESS) {
                adjustConversionRates(getValueOfBase()!!)
            }
        }
        this.conversionRatesResource!!.observeForever(observer)
    }

    /*fun setConversionRates(rates: LiveData<ConversionRates>) {
        conversionRatesResource = rates

        rates.observe({lifecycle}) {
            adjustConversionRates(getValueOfBase()!!)
        }
    }*/

    // TODO: Round all the way down, from 3.9 to 3.0, or rather the UI simply displays only 2 decimals

    // TODO: The UI will not be reading from the repo but from my LiveData of ConversionRates
    //  that are adjusted for the new value of base

    /**
     * // TODO: Docs
     * Should the users action internally result in the changing of the value of the base currency?
     * All the user is doing is indirectly changing the value of the base currency
     * by setting the desired value of another currency
     * - Base is in EUR, starting at 1 EUR
     * - User enters 2 CZK
     * - Value of base is changed to 0.08 by 1 EUR / 25 CZK * 2 CZK
     */
    fun adjustValueOfBase(currency: Currency, value: Float) {
        if (conversionRatesResource?.value == null) return

        val unadjustedCurrencyRate = conversionRatesResource!!.value!!.data!!.rates
            .find { it.currency == currency }!!.rate

        val adjustedValueOfBase = getValueOfBase()!! / (unadjustedCurrencyRate / value)

        adjustConversionRates(adjustedValueOfBase)
    }

    private fun getValueOfBase() = conversionRatesResource?.value?.data?.let {
        it.rates.findByCurrency(it.baseCurrency.currencyCode)?.rate
    }

    private fun adjustConversionRates(value: Float) {
        val resource = conversionRatesResource?.value ?: return
        val conversionRates = resource.data ?: return

        _adjustedConversionRates.postValue(
            resource.copy(
                data = conversionRates.copy(
                    rates = conversionRates.rates.map {
                        it.copy(rate = it.rate * value)
                    }
                )
            )
        )
    }
}