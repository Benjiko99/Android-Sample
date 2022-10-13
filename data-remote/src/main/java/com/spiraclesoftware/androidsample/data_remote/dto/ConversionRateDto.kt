package com.spiraclesoftware.androidsample.data_remote.dto

import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class ConversionRateDto(
    val currency: Currency,
    val rate: Float
)