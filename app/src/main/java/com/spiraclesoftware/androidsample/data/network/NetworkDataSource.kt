package com.spiraclesoftware.androidsample.data.network

import com.spiraclesoftware.androidsample.domain.model.ConversionRates
import com.spiraclesoftware.androidsample.domain.model.Transaction
import java.util.*

class NetworkDataSource(
    private val mainApi: MainApi
) {

    suspend fun fetchTransactions(): List<Transaction> {
        return mainApi.fetchTransactions().items
    }

    suspend fun fetchConversionRates(baseCurrency: Currency): ConversionRates {
        return mainApi.fetchConversionRates(baseCurrency.currencyCode)
    }

}
