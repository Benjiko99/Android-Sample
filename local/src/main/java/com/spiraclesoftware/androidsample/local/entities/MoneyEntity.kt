package com.spiraclesoftware.androidsample.local.entities

import java.math.BigDecimal
import java.util.*

data class MoneyEntity(
    val amount: BigDecimal,
    val currency: Currency
)