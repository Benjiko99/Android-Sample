package com.spiraclesoftware.androidsample.application.data

import androidx.lifecycle.LiveData
import com.spiraclesoftware.androidsample.shared.data.dto.TransactionsResponseWrapper
import com.spiraclesoftware.androidsample.shared.domain.ConversionRates
import com.spiraclesoftware.core.data.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

    @Headers("Accept: application/json")
    @GET("transactions")
    suspend fun transactionList(): TransactionsResponseWrapper

    @Headers("Accept: application/json")
    @GET("transactions")
    fun transactionListOld(): LiveData<ApiResponse<TransactionsResponseWrapper>>

    @GET("conversion_rates")
    fun conversionRates(
        @Query("base") baseCurrencyCode: String
    ): LiveData<ApiResponse<ConversionRates>>
}