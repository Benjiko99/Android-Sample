package com.spiraclesoftware.androidsample

import com.spiraclesoftware.androidsample.domain.model.Money
import java.math.BigDecimal
import java.util.*

/** Shorthand constructor for Money */
fun money(amount: String, currencyCode: String): Money {
    return Money(BigDecimal(amount), Currency.getInstance(currencyCode))
}
