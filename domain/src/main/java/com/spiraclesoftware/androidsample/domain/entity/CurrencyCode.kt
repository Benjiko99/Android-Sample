package com.spiraclesoftware.androidsample.domain.entity

import java.util.*

typealias CurrencyCode = UniqueIdentifier<String>

fun Currency.currencyCode() = CurrencyCode(currencyCode)

fun CurrencyCode.toInstance() = Currency.getInstance(this.value)!!