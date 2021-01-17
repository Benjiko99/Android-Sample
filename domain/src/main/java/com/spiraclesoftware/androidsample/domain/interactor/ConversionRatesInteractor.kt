package com.spiraclesoftware.androidsample.domain.interactor

import com.spiraclesoftware.androidsample.domain.MemoryDataSource
import com.spiraclesoftware.androidsample.domain.RemoteDataSource
import com.spiraclesoftware.androidsample.domain.model.ConversionRates
import java.util.*

class ConversionRatesInteractor(
    private val remoteDataSource: RemoteDataSource,
    private val memoryDataSource: MemoryDataSource
) {

    suspend fun fetchConversionRates(baseCurrency: Currency): ConversionRates {
        return remoteDataSource.fetchConversionRates(baseCurrency).also {
            memoryDataSource.saveConversionRates(baseCurrency, it)
        }
    }

    suspend fun getConversionRates(baseCurrency: Currency): ConversionRates {
        val cached = memoryDataSource.getConversionRates(baseCurrency)
        return cached ?: fetchConversionRates(baseCurrency)
    }

}
