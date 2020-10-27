package com.spiraclesoftware.androidsample.data.disk

import androidx.room.TypeConverter
import com.spiraclesoftware.androidsample.domain.model.TransactionCategory
import com.spiraclesoftware.androidsample.domain.model.TransactionStatus
import com.spiraclesoftware.androidsample.domain.model.TransactionStatusCode
import com.spiraclesoftware.androidsample.domain.model.TransferDirection
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import org.koin.java.KoinJavaComponent.inject
import java.math.BigDecimal
import java.util.*

@OptIn(ExperimentalStdlibApi::class)
class Converters {

    private val moshi by inject(Moshi::class.java)

    @TypeConverter
    fun fromEnum(item: Enum<*>?): String? =
        item?.name

    @TypeConverter
    fun fromListOfString(item: List<String>?): String? =
        moshi.adapter<List<String>?>().toJson(item)

    @TypeConverter
    fun toListOfString(value: String?): List<String>? =
        if (value != null) moshi.adapter<List<String>?>().fromJson(value) else emptyList()

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