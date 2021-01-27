package com.spiraclesoftware.androidsample.data_local.entity

import java.math.BigDecimal
import java.util.*

data class MoneyEntity(
    val amount: BigDecimal,
    val currency: Currency
)