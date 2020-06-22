package com.spiraclesoftware.androidsample.shared.domain

import com.spiraclesoftware.core.data.Identifiable
import java.util.*

data class ConversionRates(
    val baseCurrency: Currency,
    val validityDate: Date,
    val rates: List<ConversionRate>
) : Identifiable<CurrencyCode> {

    override fun getUniqueId() = baseCurrency.currencyCode()
}