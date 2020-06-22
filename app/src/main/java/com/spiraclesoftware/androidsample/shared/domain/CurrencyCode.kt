package com.spiraclesoftware.androidsample.shared.domain

import com.spiraclesoftware.core.data.UniqueIdentifier
import java.util.*

typealias CurrencyCode = UniqueIdentifier<String>

fun Currency.currencyCode() = CurrencyCode(currencyCode)

fun CurrencyCode.toInstance() = Currency.getInstance(this.value)!!