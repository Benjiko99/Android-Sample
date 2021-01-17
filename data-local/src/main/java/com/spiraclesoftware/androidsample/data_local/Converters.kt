package com.spiraclesoftware.androidsample.data_local

import android.net.Uri
import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.math.BigDecimal
import java.util.*

class Converters {

    @TypeConverter
    fun fromEnum(item: Enum<*>?): String? =
        item?.name

    @TypeConverter
    fun fromListOfString(item: List<String>): String =
        Json.encodeToString(item)

    @TypeConverter
    fun toListOfString(value: String): List<String> =
        Json.decodeFromString(value)

    @TypeConverter
    fun fromListOfUri(item: List<Uri>): String =
        Json.encodeToString(item.map { it.toString() })

    @TypeConverter
    fun toListOfUri(value: String): List<Uri> =
        Json.decodeFromString<List<String>>(value).map { Uri.parse(it) }

    @TypeConverter
    fun toBigDecimal(value: String?) =
        value?.let { BigDecimal(value) }

    @TypeConverter
    fun fromBigDecimal(item: BigDecimal?) =
        item?.toPlainString()

    @TypeConverter
    fun toCurrency(value: String?) =
        value?.let { Currency.getInstance(value) }

    @TypeConverter
    fun fromCurrency(item: Currency?) =
        item?.currencyCode

}