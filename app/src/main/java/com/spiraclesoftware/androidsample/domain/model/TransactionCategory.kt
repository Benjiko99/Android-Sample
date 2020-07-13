package com.spiraclesoftware.androidsample.domain.model

import com.spiraclesoftware.androidsample.R

enum class TransactionCategory {
    ENTERTAINMENT, RESTAURANTS, GROCERIES, TRANSFERS;

    val stringRes: Int
        get() = when (this) {
            ENTERTAINMENT -> R.string.transaction_category__entertainment
            RESTAURANTS -> R.string.transaction_category__restaurants
            GROCERIES -> R.string.transaction_category__groceries
            TRANSFERS -> R.string.transaction_category__transfers
        }

    val drawableRes: Int
        get() = when (this) {
            ENTERTAINMENT -> R.drawable.ic_category_entertainment
            RESTAURANTS -> R.drawable.ic_category_restaurants
            GROCERIES -> R.drawable.ic_category_groceries
            TRANSFERS -> R.drawable.ic_category_transfers
        }

    val colorRes: Int
        get() = when (this) {
            ENTERTAINMENT -> R.color.transaction_category__entertainment
            RESTAURANTS -> R.color.transaction_category__restaurants
            GROCERIES -> R.color.transaction_category__groceries
            TRANSFERS -> R.color.transaction_category__transfers
        }
}