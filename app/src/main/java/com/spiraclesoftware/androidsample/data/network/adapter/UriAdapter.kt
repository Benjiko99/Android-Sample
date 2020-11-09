package com.spiraclesoftware.androidsample.data.network.adapter

import android.net.Uri
import androidx.core.net.toUri
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class UriAdapter {

    @FromJson
    fun fromJson(json: String): Uri {
        return json.toUri()
    }

    @ToJson
    fun toJson(value: Uri): String {
        return value.toString()
    }

}