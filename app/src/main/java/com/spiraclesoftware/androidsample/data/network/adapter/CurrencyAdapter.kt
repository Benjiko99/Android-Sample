package com.spiraclesoftware.androidsample.data.network.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.util.*

class CurrencyAdapter {

    @FromJson
    fun fromJson(json: String?): Currency? {
        return Currency.getInstance(json)
    }

    @ToJson
    fun toJson(value: Currency?): String? {
        return value?.currencyCode
    }

}