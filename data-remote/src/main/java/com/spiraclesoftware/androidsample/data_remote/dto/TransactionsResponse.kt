package com.spiraclesoftware.androidsample.data_remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class TransactionsResponse(
    val items: List<TransactionDto>
)
