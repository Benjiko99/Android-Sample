package com.spiraclesoftware.airbankinterview.application.api

import androidx.lifecycle.LiveData
import com.spiraclesoftware.airbankinterview.shared.domain.TransactionDetail
import com.spiraclesoftware.airbankinterview.features.transaction.list.data.dto.TransactionListResponse
import com.spiraclesoftware.core.data.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface ApiService {

    @Headers("Accept: application/json")
    @GET("transactions")
    fun transactionList(): LiveData<ApiResponse<TransactionListResponse>>

    @Headers("Accept: application/json")
    @GET("transactions/{transaction_id}")
    fun transactionDetail(@Path("transaction_id") transactionId: Int): LiveData<ApiResponse<TransactionDetail>>
}