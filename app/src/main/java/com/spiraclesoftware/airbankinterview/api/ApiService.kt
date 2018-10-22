package com.spiraclesoftware.airbankinterview.api

import androidx.lifecycle.LiveData
import com.spiraclesoftware.airbankinterview.transaction.list.domain.TransactionListResponse
import com.spiraclesoftware.core.data.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiService {

    @Headers("Accept: application/json")
    @GET("transactions")
    fun transactionList(): LiveData<ApiResponse<TransactionListResponse>>
}