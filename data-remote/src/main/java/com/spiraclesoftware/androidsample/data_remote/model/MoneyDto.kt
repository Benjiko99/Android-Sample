package com.spiraclesoftware.androidsample.data_remote.model

import com.squareup.moshi.JsonClass
import java.math.BigDecimal
import java.util.*

@JsonClass(generateAdapter = true)
data class MoneyDto(
    val amount: BigDecimal,
    val currency: Currency
)