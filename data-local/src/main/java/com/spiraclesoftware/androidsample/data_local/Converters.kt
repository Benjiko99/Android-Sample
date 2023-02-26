package com.spiraclesoftware.androidsample.data_local

import androidx.room.TypeConverter
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.math.BigDecimal
import java.util.Currency

@OptIn(ExperimentalSerializationApi::class)
class Converters {

    @TypeConverter
    fun fromListOfString(item: List<String>): String =
        Json.encodeToString(item)

    @TypeConverter
    fun toListOfString(value: String): List<String> =
        Json.decodeFromString(value)

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