package com.spiraclesoftware.androidsample.application.extensions

import com.spiraclesoftware.androidsample.shared.domain.ConversionRate

fun List<ConversionRate>.findByCurrency(code: String) =
    this.find { it.currency.currencyCode == code }