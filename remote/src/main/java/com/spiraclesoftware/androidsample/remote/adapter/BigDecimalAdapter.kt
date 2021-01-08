package com.spiraclesoftware.androidsample.remote.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.math.BigDecimal

class BigDecimalAdapter {

    @FromJson
    fun fromJson(json: String?): BigDecimal? {
        return BigDecimal(json)
    }

    @ToJson
    fun toJson(value: BigDecimal?): String? {
        return value?.toPlainString()
    }

}