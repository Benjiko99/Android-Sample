package com.spiraclesoftware.androidsample.data.disk

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.spiraclesoftware.androidsample.domain.model.TransactionCategory
import com.spiraclesoftware.androidsample.domain.model.TransactionStatus
import com.spiraclesoftware.androidsample.domain.model.TransactionStatusCode
import com.spiraclesoftware.androidsample.domain.model.TransferDirection
import org.koin.java.KoinJavaComponent.inject
import java.math.BigDecimal
import java.util.*

class Converters {

    private val gson by inject(Gson::class.java)

    private inline fun <reified T> Gson.fromJson(json: String) =
        fromJson<T>(json, object : TypeToken<T>() {}.type)

    @TypeConverter
    fun fromEnum(item: Enum<*>?): String? =
        item?.name

    @TypeConverter
    fun fromList(item: List<Any>?): String? =
        gson.toJson(item)

    @TypeConverter
    fun toListOfString(value: String?): List<String>? =
        if (value != null) gson.fromJson(value) else emptyList()

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

    @TypeConverter
    fun toTransferDirection(value: String?) =
        value?.let { TransferDirection.valueOf(it) }

    @TypeConverter
    fun toTransactionCategory(value: String?) =
        value?.let { TransactionCategory.valueOf(it) }

    @TypeConverter
    fun toTransactionStatus(value: String?) =
        value?.let { TransactionStatus.valueOf(it) }

    @TypeConverter
    fun toTransactionStatusCode(value: String?) =
        value?.let { TransactionStatusCode.valueOf(it) }

}