package com.spiraclesoftware.androidsample.feature.category_select

import com.spiraclesoftware.androidsample.common.formatter.TransactionCategoryFormatter
import com.spiraclesoftware.androidsample.domain.entity.TransactionCategory
import com.spiraclesoftware.androidsample.feature.category_select.item.model.CategoryModel

class CategoryModelFormatter(
    private val categoryFormatter: TransactionCategoryFormatter,
) {

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