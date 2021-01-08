package com.spiraclesoftware.androidsample.remote.model

import com.squareup.moshi.JsonClass
import java.math.BigDecimal
import java.util.*

@JsonClass(generateAdapter = true)
data class MoneyDto(
    val amount: BigDecimal,
    val currency: Currency
)