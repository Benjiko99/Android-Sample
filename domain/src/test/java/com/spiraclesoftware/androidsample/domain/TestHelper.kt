package com.spiraclesoftware.androidsample.domain

import com.spiraclesoftware.androidsample.domain.entity.Money
import java.time.ZonedDateTime
import java.math.BigDecimal
import java.util.*

val epochDateTime = ZonedDateTime.parse("1970-01-01T00:00:00+00:00")!!

/** Shorthand constructor for Money */
fun money(amount: String, currencyCode: String): Money {
    return Money(BigDecimal(amount), Currency.getInstance(currencyCode))
}
