package com.spiraclesoftware.androidsample.domain.interactor

import com.spiraclesoftware.androidsample.data.disk.DiskDataSource
import com.spiraclesoftware.androidsample.data.network.NetworkDataSource
import com.spiraclesoftware.androidsample.domain.model.ConversionRates
import com.spiraclesoftware.androidsample.domain.model.CurrencyCode

class ConversionRatesInteractor(
    private val networkDataSource: NetworkDataSource,
    private val diskDataSource: DiskDataSource
) {

    suspend fun getConversionRates(
        baseCurrency: CurrencyCode,
        ignoreCached: Boolean = false
    ): ConversionRates {
        suspend fun getFromNetwork() =
            networkDataSource.getConversionRates(baseCurrency).also {
                diskDataSource.saveConversionRates(baseCurrency, it)
            }

        return if (ignoreCached) getFromNetwork()
        else diskDataSource.getConversionRates(baseCurrency) ?: getFromNetwork()
    }

}
