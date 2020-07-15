package com.spiraclesoftware.androidsample.data.network

import com.spiraclesoftware.androidsample.data.network.model.TransactionsResponseWrapper
import com.spiraclesoftware.androidsample.domain.model.ConversionRates
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface MainApi {

    @Headers("Accept: application/json")
    @GET("transactions")
    suspend fun fetchTransactions(): TransactionsResponseWrapper

    @GET("conversion_rates")
    suspend fun fetchConversionRates(
        @Query("base") baseCurrencyCode: String
    ): ConversionRates

}