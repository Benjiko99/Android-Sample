package com.spiraclesoftware.androidsample.data.network.model

import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TransactionsResponseWrapper(
    val items: List<Transaction>
)
