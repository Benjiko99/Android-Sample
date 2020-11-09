package com.spiraclesoftware.androidsample.data.network

import android.net.Uri
import com.spiraclesoftware.androidsample.data.disk.DiskDataSource
import com.spiraclesoftware.androidsample.data.network.model.TransactionUpdateRequest
import com.spiraclesoftware.androidsample.domain.model.ConversionRates
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.domain.model.TransactionId
import org.koin.java.KoinJavaComponent.inject
import java.util.*

class NetworkDataSource(
    private val mainApi: MainApi
) {

    suspend fun fetchTransactions(): List<Transaction> {
        return mainApi.fetchTransactions().items
    }

    suspend fun fetchConversionRates(baseCurrency: Currency): ConversionRates {
        return mainApi.fetchConversionRates(baseCurrency.currencyCode)
    }

    suspend fun updateTransaction(id: TransactionId, request: TransactionUpdateRequest): Transaction {
        // NOTE: We don't have a backend for this feature yet, pretend we got a successful response
        val diskDataSource = inject(DiskDataSource::class.java).value
        val transaction = diskDataSource.getTransactionById(id)

        return when {
            request.noteToSelf != null -> {
                val noteOrNull = if (request.noteToSelf.isBlank()) null else request.noteToSelf
                transaction!!.copy(noteToSelf = noteOrNull)
            }
            request.category != null -> transaction!!.copy(category = request.category)
            else -> transaction!!
        }

        // TODO: Once we have a backend, use this code, and make the note nullable
        //return mainApi.updateTransaction(id.value, request)
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
