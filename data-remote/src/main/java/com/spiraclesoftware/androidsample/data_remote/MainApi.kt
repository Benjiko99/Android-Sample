package com.spiraclesoftware.androidsample.data_remote

import android.net.Uri
import com.spiraclesoftware.androidsample.data_remote.model.*
import okhttp3.MultipartBody
import retrofit2.http.*
import java.util.*

interface MainApi {

    @GET("transactions")
    suspend fun fetchTransactions(): TransactionsResponseWrapper

    @GET("conversion-rates")
    suspend fun fetchConversionRates(
        @Query("baseCurrency") baseCurrency: Currency
    ): ConversionRatesDto

    @PATCH("transactions/{id}/note")
    suspend fun updateTransactionNote(
        @Path("id") id: String,
        @Body note: String
    ): TransactionDto

    @PATCH("transactions/{id}/category")
    suspend fun updateTransactionCategory(
        @Path("id") id: String,
        @Body category: String
    ): TransactionDto

    /** @return URL at which the uploaded image is hosted */
    @Multipart
    @POST("transactions/{id}/upload-attachment")
    suspend fun uploadAttachment(
        @Path("id") id: String,
        @Part image: MultipartBody.Part
    ): Uri

    @POST("transactions/{id}/remove-attachment")
    suspend fun removeAttachment(
        @Path("id") id: String,
        @Body uri: Uri
    )

}