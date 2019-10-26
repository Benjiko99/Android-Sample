package com.spiraclesoftware.androidsample.features.rates.converter

import androidx.lifecycle.ViewModel
import com.spiraclesoftware.androidsample.shared.data.ConversionRatesRepository
import com.spiraclesoftware.androidsample.shared.domain.CurrencyCode

class RatesConverterViewModel(
    private val ratesRepo: ConversionRatesRepository
) : ViewModel() {

}