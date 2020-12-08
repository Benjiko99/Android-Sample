package com.spiraclesoftware.androidsample.data.network

import android.net.Uri
import com.spiraclesoftware.androidsample.data.network.model.TransactionsResponseWrapper
import com.spiraclesoftware.androidsample.domain.model.ConversionRates
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.domain.model.TransactionCategory
import com.spiraclesoftware.androidsample.domain.model.TransactionId
import okhttp3.MultipartBody
import retrofit2.http.*
import java.util.*

interface MainApi {

    @GET("transactions")
    suspend fun fetchTransactions(): TransactionsResponseWrapper

    @GET("conversion-rates")
    suspend fun fetchConversionRates(
        @Query("baseCurrency") baseCurrency: Currency
    ): ConversionRates

    @PATCH("transactions/{id}/note")
    suspend fun updateTransactionNote(
        @Path("id") id: TransactionId,
        @Body note: String
    ): Transaction

    @PATCH("transactions/{id}/category")
    suspend fun updateTransactionCategory(
        @Path("id") id: TransactionId,
        @Body category: TransactionCategory
    ): Transaction

    /** @return URL at which the uploaded image is hosted */
    @Multipart
    @POST("transactions/{id}/upload-attachment")
    suspend fun uploadAttachment(
        @Path("id") id: TransactionId,
        @Part image: MultipartBody.Part
    ): Uri

    @POST("transactions/{id}/remove-attachment")
    suspend fun removeAttachment(
        @Path("id") id: TransactionId,
        @Body uri: Uri
    )

}