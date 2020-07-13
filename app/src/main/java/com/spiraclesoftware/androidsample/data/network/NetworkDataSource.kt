package com.spiraclesoftware.androidsample.data.network

import com.spiraclesoftware.androidsample.domain.model.ConversionRates
import com.spiraclesoftware.androidsample.domain.model.CurrencyCode
import com.spiraclesoftware.androidsample.domain.model.Transaction

class NetworkDataSource(
    private val mainApi: MainApi
) {

    suspend fun getTransactions(): List<Transaction> {
        return mainApi.getTransactions().items
    }

    suspend fun getConversionRates(baseCurrency: CurrencyCode): ConversionRates {
        return mainApi.getConversionRates(baseCurrency.value)
    }

}
