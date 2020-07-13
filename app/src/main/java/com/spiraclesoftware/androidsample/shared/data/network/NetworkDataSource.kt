package com.spiraclesoftware.androidsample.shared.data.network

import com.spiraclesoftware.androidsample.application.data.ApiService
import com.spiraclesoftware.androidsample.shared.domain.ConversionRates
import com.spiraclesoftware.androidsample.shared.domain.CurrencyCode
import com.spiraclesoftware.androidsample.shared.domain.Transaction

class NetworkDataSource(
    private val apiService: ApiService
) {

    suspend fun getTransactions(): List<Transaction> {
        return apiService.transactionList().items
    }

    suspend fun getConversionRates(baseCurrency: CurrencyCode): ConversionRates {
        return apiService.conversionRates(baseCurrency.value)
    }

}
