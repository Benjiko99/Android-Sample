package com.spiraclesoftware.androidsample.data.network

import android.net.Uri
import com.spiraclesoftware.androidsample.domain.model.ConversionRates
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.domain.model.TransactionCategory
import com.spiraclesoftware.androidsample.domain.model.TransactionId
import java.util.*

class NetworkDataSource(
    private val mainApi: MainApi
) {

    suspend fun fetchTransactions(): List<Transaction> {
        return mainApi.fetchTransactions().items
    }

    suspend fun fetchConversionRates(baseCurrency: Currency): ConversionRates {
        return mainApi.fetchConversionRates(baseCurrency)
    }

    suspend fun updateTransactionNote(id: TransactionId, note: String): Transaction {
        return mainApi.updateTransactionNote(id, note)
    }

    suspend fun updateTransactionCategory(id: TransactionId, category: TransactionCategory): Transaction {
        return mainApi.updateTransactionCategory(id, category)
    }

    suspend fun removeAttachment(id: TransactionId, uri: Uri) {
        // NOTE: We don't have a backend for this feature yet, pretend we got a successful response.
        // Success is indicated by not throwing an exception, so we'll simply do nothing here.

        // TODO: Once we have a backend, use this code
        //return mainApi.removeAttachment(id.value, uri)
    }

    suspend fun uploadAttachment(id: TransactionId, uri: Uri): Uri {
        // NOTE: We don't have a backend for this feature yet, pretend we got a successful response
        return uri

        // TODO: Once we have a backend, use this code
//        val image = uri.toFile().asRequestBody()
//        return mainApi.uploadAttachment(id.value, image)
    }

}
