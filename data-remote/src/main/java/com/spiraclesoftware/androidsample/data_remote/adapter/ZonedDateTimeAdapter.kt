package com.spiraclesoftware.androidsample.data_remote.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ZonedDateTimeAdapter {

    @FromJson
    fun fromJson(json: String?): ZonedDateTime? {
        return ZonedDateTime.parse(json)
    }

    @ToJson
    fun toJson(value: ZonedDateTime?): String? {
        return value?.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
    }

}