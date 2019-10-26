package com.spiraclesoftware.androidsample.features.rates

import com.spiraclesoftware.androidsample.features.rates.converter.RatesConverterViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ratesModule = module {

    viewModel { RatesConverterViewModel(get()) }
}