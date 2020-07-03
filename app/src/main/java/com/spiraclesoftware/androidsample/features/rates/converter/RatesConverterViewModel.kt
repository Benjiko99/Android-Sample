package com.spiraclesoftware.androidsample.features.rates.converter

import androidx.lifecycle.ViewModel

class RatesConverterViewModel(
    ratesConverter: RatesConverter
) : ViewModel() {

    // TODO: I feel like the UI should be getting the pre-adjustment rates for list items,
    //  and the adjusted rates will be sent to them as separate date that they keep refreshing
    val conversionRates = ratesConverter.adjustedConversionRates
}