package com.spiraclesoftware.androidsample.data.network.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl

class HttpUrlAdapter {

    @FromJson
    fun fromJson(json: String): HttpUrl {
        return json.toHttpUrl()
    }

    @ToJson
    fun toJson(value: HttpUrl): String {
        return value.toString()
    }

}