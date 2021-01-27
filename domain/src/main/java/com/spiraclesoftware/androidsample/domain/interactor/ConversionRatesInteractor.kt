package com.spiraclesoftware.androidsample.domain.interactor

import com.spiraclesoftware.androidsample.domain.LocalDataSource
import com.spiraclesoftware.androidsample.domain.RemoteDataSource
import com.spiraclesoftware.androidsample.domain.entity.ConversionRates
import java.util.*

class ConversionRatesInteractor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) {

    suspend fun fetchConversionRates(baseCurrency: Currency): ConversionRates {
        return remoteDataSource.fetchConversionRates(baseCurrency).also {
            localDataSource.saveConversionRates(baseCurrency, it)
        }
    }

    suspend fun getConversionRates(baseCurrency: Currency): ConversionRates {
        val cached = localDataSource.getConversionRates(baseCurrency)
        return cached ?: fetchConversionRates(baseCurrency)
    }

}
