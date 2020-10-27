package com.spiraclesoftware.androidsample.data.network.model

import com.spiraclesoftware.androidsample.domain.model.TransactionCategory
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TransactionUpdateRequest(
    val category: TransactionCategory? = null,
    val noteToSelf: String? = null
)