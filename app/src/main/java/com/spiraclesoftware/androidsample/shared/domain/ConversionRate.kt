package com.spiraclesoftware.androidsample.shared.domain

import com.spiraclesoftware.core.data.Identifiable
import java.util.*

data class ConversionRate(
    val currency: Currency,
    val rate: Float
) : Identifiable<CurrencyCode> {

    override fun getUniqueId() = currency.currencyCode()
}