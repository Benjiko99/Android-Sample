package com.spiraclesoftware.androidsample.data.network

import android.net.Uri
import com.spiraclesoftware.androidsample.data.network.model.TransactionUpdateRequest
import com.spiraclesoftware.androidsample.data.network.model.TransactionsResponseWrapper
import com.spiraclesoftware.androidsample.domain.model.ConversionRates
import com.spiraclesoftware.androidsample.domain.model.Transaction
import okhttp3.MultipartBody
import retrofit2.http.*

interface MainApi {

    @GET("transactions")
    suspend fun fetchTransactions(): TransactionsResponseWrapper

    @GET("conversion_rates")
    suspend fun fetchConversionRates(
        @Query("base") baseCurrencyCode: String
    ): ConversionRates

    @PUT("transactions/{id}")
    suspend fun updateTransaction(
        @Path("id") id: Int,
        @Body body: TransactionUpdateRequest
    ): Transaction

    /** @return URL at which the uploaded image is hosted */
    @Multipart
    @POST("transactions/{id}/upload_attachment")
    suspend fun uploadAttachment(
        @Path("id") id: Int,
        @Part image: MultipartBody.Part
    ): Uri

    @POST("transactions/{id}/remove_attachment")
    suspend fun removeAttachment(
        @Path("id") id: Int,
        @Body uri: Uri
    )

}