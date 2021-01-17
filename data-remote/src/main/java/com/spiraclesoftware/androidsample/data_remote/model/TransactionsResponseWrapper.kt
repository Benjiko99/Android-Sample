package com.spiraclesoftware.androidsample.data_remote.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class TransactionsResponseWrapper(
    val items: List<TransactionDto>
)
