package com.spiraclesoftware.androidsample.formatter

import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.domain.entity.TransactionCategory
import com.spiraclesoftware.androidsample.domain.entity.TransactionStatus
import com.spiraclesoftware.androidsample.domain.entity.TransactionStatusCode

val TransactionCategory.stringRes: Int
    get() = when (this) {
        TransactionCategory.ENTERTAINMENT -> R.string.transaction_category__entertainment
        TransactionCategory.RESTAURANTS -> R.string.transaction_category__restaurants
        TransactionCategory.GROCERIES -> R.string.transaction_category__groceries
        TransactionCategory.TRANSFERS -> R.string.transaction_category__transfers
    }

val TransactionCategory.drawableRes: Int
    get() = when (this) {
        TransactionCategory.ENTERTAINMENT -> R.drawable.ic_category_entertainment
        TransactionCategory.RESTAURANTS -> R.drawable.ic_category_restaurants
        TransactionCategory.GROCERIES -> R.drawable.ic_category_groceries
        TransactionCategory.TRANSFERS -> R.drawable.ic_category_transfers
    }

val TransactionCategory.colorRes: Int
    get() = when (this) {
        TransactionCategory.ENTERTAINMENT -> R.color.transaction_category__entertainment
        TransactionCategory.RESTAURANTS -> R.color.transaction_category__restaurants
        TransactionCategory.GROCERIES -> R.color.transaction_category__groceries
        TransactionCategory.TRANSFERS -> R.color.transaction_category__transfers
    }

val TransactionStatusCode.stringRes: Int?
    get() = when (this) {
        TransactionStatusCode.SPENDING_LIMIT_EXCEEDED -> R.string.transaction_status_code__spending_limit_exceeded
        else -> null
    }

val TransactionStatus.stringRes: Int
    get() = when (this) {
        TransactionStatus.COMPLETED -> R.string.transaction_status__completed
        TransactionStatus.DECLINED -> R.string.transaction_status__declined
    }
