package com.spiraclesoftware.androidsample.domain

import android.net.Uri
import com.spiraclesoftware.androidsample.domain.model.ConversionRates
import com.spiraclesoftware.androidsample.domain.model.Transaction
import java.util.*

interface NetworkDataSource {
    suspend fun fetchTransactions(): List<Transaction>

    suspend fun fetchConversionRates(baseCurrency: Currency): ConversionRates

    suspend fun updateTransactionNote(id: String, note: String): Transaction

    suspend fun updateTransactionCategory(id: String, category: String): Transaction

    suspend fun removeAttachment(id: String, uri: Uri)

    suspend fun uploadAttachment(id: String, uri: Uri): Uri
}