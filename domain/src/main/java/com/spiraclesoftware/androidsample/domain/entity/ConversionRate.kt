package com.spiraclesoftware.androidsample.domain.entity

import java.util.*

data class ConversionRate(
    val currency: Currency,
    val rate: Float
) : Identifiable<CurrencyCode> {

    constructor(currencyCode: String, rate: Float) : this(Currency.getInstance(currencyCode), rate)

    override fun getUniqueId() = currency.currencyCode()

}