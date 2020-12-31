package com.spiraclesoftware.androidsample.data.network.adapter

import com.spiraclesoftware.androidsample.domain.model.CurrencyCode
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class CurrencyCodeAdapter {

    @FromJson
    fun fromJson(json: String): CurrencyCode {
        return CurrencyCode(json)
    }

    @ToJson
    fun toJson(value: CurrencyCode): String {
        return value.value
    }

}