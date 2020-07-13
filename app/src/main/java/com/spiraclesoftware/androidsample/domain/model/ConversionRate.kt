package com.spiraclesoftware.androidsample.domain.model

import com.spiraclesoftware.core.domain.Identifiable
import java.util.*

data class ConversionRate(
    val currency: Currency,
    val rate: Float
) : Identifiable<CurrencyCode> {

    override fun getUniqueId() = currency.currencyCode()

}