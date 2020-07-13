package com.spiraclesoftware.androidsample.domain.model

import com.spiraclesoftware.core.domain.UniqueIdentifier
import java.util.*

typealias CurrencyCode = UniqueIdentifier<String>

fun Currency.currencyCode() = CurrencyCode(currencyCode)

fun CurrencyCode.toInstance() = Currency.getInstance(this.value)!!