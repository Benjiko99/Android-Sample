package com.spiraclesoftware.androidsample.domain.model

import com.squareup.moshi.JsonClass
import java.math.BigDecimal
import java.util.*

@JsonClass(generateAdapter = true)
data class Money(
    val amount: BigDecimal,
    val currency: Currency
)
