package com.spiraclesoftware.androidsample.data.network

import com.spiraclesoftware.androidsample.data.network.model.TransactionsResponseWrapper
import com.spiraclesoftware.androidsample.data.network.model.TransactionUpdateRequest
import com.spiraclesoftware.androidsample.domain.model.ConversionRates
import com.spiraclesoftware.androidsample.domain.model.Transaction
import retrofit2.http.*

interface MainApi {

    @GET("transactions")
    suspend fun fetchTransactions(): TransactionsResponseWrapper

    @PUT("transactions/{id}")
    suspend fun updateTransaction(
        @Path("id") id: Int,
        @Body body: TransactionUpdateRequest
    ): Transaction

    @GET("conversion_rates")
    suspend fun fetchConversionRates(
        @Query("base") baseCurrencyCode: String
    ): ConversionRates

}