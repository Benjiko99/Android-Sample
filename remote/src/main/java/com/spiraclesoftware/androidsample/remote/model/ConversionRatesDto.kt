package com.spiraclesoftware.androidsample.remote.model

import com.squareup.moshi.JsonClass
import org.threeten.bp.ZonedDateTime
import java.util.*

@JsonClass(generateAdapter = true)
data class ConversionRatesDto(
    val baseCurrency: Currency,
    val validityDate: ZonedDateTime,
    val rates: List<ConversionRateDto>
)