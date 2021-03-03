package com.spiraclesoftware.androidsample.formatter

import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.domain.entity.TransactionCategory

class TransactionCategoryFormatter {

    fun stringRes(category: TransactionCategory) = when (category) {
        TransactionCategory.ENTERTAINMENT -> R.string.transaction_category__entertainment
        TransactionCategory.RESTAURANTS -> R.string.transaction_category__restaurants
        TransactionCategory.GROCERIES -> R.string.transaction_category__groceries
        TransactionCategory.TRANSFERS -> R.string.transaction_category__transfers
    }

    fun drawableRes(category: TransactionCategory) = when (category) {
        TransactionCategory.ENTERTAINMENT -> R.drawable.ic_category_entertainment
        TransactionCategory.RESTAURANTS -> R.drawable.ic_category_restaurants
        TransactionCategory.GROCERIES -> R.drawable.ic_category_groceries
        TransactionCategory.TRANSFERS -> R.drawable.ic_category_transfers
    }

    fun colorRes(category: TransactionCategory) = when (category) {
        TransactionCategory.ENTERTAINMENT -> R.color.transaction_category__entertainment
        TransactionCategory.RESTAURANTS -> R.color.transaction_category__restaurants
        TransactionCategory.GROCERIES -> R.color.transaction_category__groceries
        TransactionCategory.TRANSFERS -> R.color.transaction_category__transfers
    }

}