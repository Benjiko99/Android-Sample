package com.spiraclesoftware.androidsample.domain.model

import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class ConversionRate(
    val currency: Currency,
    val rate: Float
) : Identifiable<CurrencyCode> {

    constructor(currencyCode: String, rate: Float) : this(Currency.getInstance(currencyCode), rate)

    override fun getUniqueId() = currency.currencyCode()

}