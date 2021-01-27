package com.spiraclesoftware.androidsample.domain

import com.spiraclesoftware.androidsample.domain.entity.ConversionRates
import com.spiraclesoftware.androidsample.domain.entity.Transaction
import kotlinx.coroutines.flow.Flow
import java.util.*

interface RemoteDataSource {
    suspend fun fetchTransactions(): Flow<Result<List<Transaction>>>

    suspend fun fetchConversionRates(baseCurrency: Currency): ConversionRates

    suspend fun updateTransactionNote(id: String, note: String): Transaction

    suspend fun updateTransactionCategory(id: String, category: String): Transaction

    suspend fun removeAttachment(id: String, uri: String)

    suspend fun uploadAttachment(id: String, uri: String): String
}