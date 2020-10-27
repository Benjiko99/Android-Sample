package com.spiraclesoftware.androidsample.data.network.adapter

import com.spiraclesoftware.androidsample.domain.model.CurrencyCode
import com.spiraclesoftware.androidsample.domain.model.TransactionId
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class UniqueIdentifierAdapter {

    @FromJson
    fun fromJsonString(json: String?): CurrencyCode? {
        return json?.let { CurrencyCode(json) }
    }

    @ToJson
    fun toJsonString(value: CurrencyCode?): String? {
        return value?.value
    }

    @FromJson
    fun fromJsonInt(json: String?): TransactionId? {
        return json?.let { TransactionId(json.toInt()) }
    }

    @ToJson
    fun toJsonInt(value: TransactionId?): String? {
        return value?.value.toString()
    }

}