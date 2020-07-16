package com.spiraclesoftware.androidsample.data.disk

import androidx.room.TypeConverter
import com.spiraclesoftware.androidsample.domain.model.TransactionCategory
import com.spiraclesoftware.androidsample.domain.model.TransactionStatus
import com.spiraclesoftware.androidsample.domain.model.TransactionStatusCode
import com.spiraclesoftware.androidsample.domain.model.TransferDirection
import java.math.BigDecimal
import java.util.*

class Converters {

    @TypeConverter
    fun toBigDecimal(value: String?): BigDecimal? {
        return value?.let { BigDecimal(value) }
    }

    @TypeConverter
    fun fromBigDecimal(item: BigDecimal?): String? {
        return item?.toPlainString()
    }

    @TypeConverter
    fun toCurrency(value: String?): Currency? {
        return value?.let { Currency.getInstance(value) }
    }

    @TypeConverter
    fun fromCurrency(item: Currency?): String? {
        return item?.currencyCode
    }

    @TypeConverter
    fun toTransferDirection(value: String?): TransferDirection? {
        return value?.let { TransferDirection.valueOf(it) }
    }

    @TypeConverter
    fun fromTransferDirection(item: TransferDirection?): String? {
        return item?.name
    }

    @TypeConverter
    fun toTransactionCategory(value: String?): TransactionCategory? {
        return value?.let { TransactionCategory.valueOf(it) }
    }

    @TypeConverter
    fun fromTransactionCategory(item: TransactionCategory?): String? {
        return item?.name
    }

    @TypeConverter
    fun toTransactionStatus(value: String?): TransactionStatus? {
        return value?.let { TransactionStatus.valueOf(it) }
    }

    @TypeConverter
    fun fromTransactionStatus(item: TransactionStatus?): String? {
        return item?.name
    }

    @TypeConverter
    fun toTransactionStatusCode(value: String?): TransactionStatusCode? {
        return value?.let { TransactionStatusCode.valueOf(it) }
    }

    @TypeConverter
    fun fromTransactionStatusCode(item: TransactionStatusCode?): String? {
        return item?.name
    }

}