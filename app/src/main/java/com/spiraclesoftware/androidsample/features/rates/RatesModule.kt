package com.spiraclesoftware.androidsample.features.rates

import com.spiraclesoftware.androidsample.features.rates.converter.RatesConverter
import com.spiraclesoftware.androidsample.features.rates.converter.RatesConverterViewModel
import com.spiraclesoftware.androidsample.shared.domain.CurrencyCode
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val ratesModule = module {

    viewModel { (baseCurrency: CurrencyCode) ->
        RatesConverterViewModel(get { parametersOf(baseCurrency) })
    }

    single { (baseCurrencyCode: CurrencyCode) ->
        RatesConverter(get(), baseCurrencyCode)
    }
}