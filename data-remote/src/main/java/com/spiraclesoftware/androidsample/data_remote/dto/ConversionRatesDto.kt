package com.spiraclesoftware.androidsample.data_remote.dto

import com.squareup.moshi.JsonClass
import java.time.ZonedDateTime
import java.util.*

@JsonClass(generateAdapter = true)
data class ConversionRatesDto(
    val baseCurrency: Currency,
    val validityDate: ZonedDateTime,
    val rates: List<ConversionRateDto>
)