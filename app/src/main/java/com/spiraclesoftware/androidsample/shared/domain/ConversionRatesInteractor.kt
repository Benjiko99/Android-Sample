package com.spiraclesoftware.androidsample.shared.domain

import com.spiraclesoftware.androidsample.shared.data.disk.DiskDataSource
import com.spiraclesoftware.androidsample.shared.data.network.NetworkDataSource

class ConversionRatesInteractor(
    private val networkDataSource: NetworkDataSource,
    private val diskDataSource: DiskDataSource
) {

    suspend fun getConversionRates(
        baseCurrency: CurrencyCode,
        ignoreCached: Boolean = false
    ): ConversionRates? {
        suspend fun getFromNetwork() =
            networkDataSource.getConversionRates(baseCurrency).also {
                diskDataSource.saveConversionRates(baseCurrency, it)
            }

        return if (ignoreCached) getFromNetwork()
        else diskDataSource.getConversionRates(baseCurrency) ?: getFromNetwork()
    }

}
