package com.spiraclesoftware.androidsample.remote.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class TransactionsResponseWrapper(
    val items: List<TransactionDto>
)
