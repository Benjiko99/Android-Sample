package com.spiraclesoftware.androidsample.data_remote.model

import com.squareup.moshi.JsonClass
import org.threeten.bp.ZonedDateTime

@JsonClass(generateAdapter = true)
data class TransactionDto(
    val id: String,
    val name: String,
    val processingDate: ZonedDateTime,
    val money: MoneyDto,
    val transferDirection: String,
    val category: String,
    val status: String,
    val statusCode: String,
    val attachments: List<String>,
    val cardDescription: String?,
    val noteToSelf: String?
)