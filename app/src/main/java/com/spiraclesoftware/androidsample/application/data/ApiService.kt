package com.spiraclesoftware.androidsample.application.data

import com.spiraclesoftware.androidsample.shared.data.dto.TransactionsResponseWrapper
import com.spiraclesoftware.androidsample.shared.domain.ConversionRates
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

    @Headers("Accept: application/json")
    @GET("transactions")
    suspend fun transactionList(): TransactionsResponseWrapper

    @GET("conversion_rates")
    suspend fun conversionRates(
        @Query("base") baseCurrencyCode: String
    ): ConversionRates
}