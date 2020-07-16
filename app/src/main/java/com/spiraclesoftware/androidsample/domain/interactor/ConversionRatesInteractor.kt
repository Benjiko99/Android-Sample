package com.spiraclesoftware.androidsample.domain.interactor

import com.spiraclesoftware.androidsample.data.memory.MemoryDataSource
import com.spiraclesoftware.androidsample.data.network.NetworkDataSource
import com.spiraclesoftware.androidsample.domain.model.ConversionRates
import com.spiraclesoftware.androidsample.domain.model.CurrencyCode

class ConversionRatesInteractor(
    private val networkDataSource: NetworkDataSource,
    private val memoryDataSource: MemoryDataSource
) {

    suspend fun getConversionRates(
        baseCurrency: CurrencyCode,
        ignoreCache: Boolean = false
    ): ConversionRates {
        suspend fun getFromNetwork() =
            networkDataSource.getConversionRates(baseCurrency).also {
                memoryDataSource.saveConversionRates(baseCurrency, it)
            }

        return if (ignoreCache) getFromNetwork()
        else memoryDataSource.getConversionRates(baseCurrency) ?: getFromNetwork()
    }

}
