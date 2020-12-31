package com.spiraclesoftware.androidsample.data.network.adapter

import com.spiraclesoftware.androidsample.domain.model.TransactionId
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class TransactionIdAdapter {

    @FromJson
    fun fromJson(json: String): TransactionId {
        return TransactionId(json)
    }

    @ToJson
    fun toJson(value: TransactionId): String {
        return value.value
    }

}