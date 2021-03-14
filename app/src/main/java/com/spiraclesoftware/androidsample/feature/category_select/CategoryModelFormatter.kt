package com.spiraclesoftware.androidsample.feature.category_select

import com.spiraclesoftware.androidsample.domain.entity.TransactionCategory
import com.spiraclesoftware.androidsample.formatter.TransactionCategoryFormatter
import org.koin.java.KoinJavaComponent.inject

class CategoryModelFormatter {

    private val categoryFormatter by inject(TransactionCategoryFormatter::class.java)

    fun format(
        category: TransactionCategory,
        isSelectedCategory: Boolean
    ): CategoryModel {
        return CategoryModel(
            ordinal = category.ordinal,
            nameRes = categoryFormatter.stringRes(category),
            iconRes = categoryFormatter.drawableRes(category),
            iconTintRes = categoryFormatter.colorRes(category),
            isSelectedCategory = isSelectedCategory
        )
    }

}