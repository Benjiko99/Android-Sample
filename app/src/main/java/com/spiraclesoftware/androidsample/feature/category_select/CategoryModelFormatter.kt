package com.spiraclesoftware.androidsample.feature.category_select

import com.spiraclesoftware.androidsample.domain.entity.TransactionCategory
import com.spiraclesoftware.androidsample.formatter.colorRes
import com.spiraclesoftware.androidsample.formatter.drawableRes
import com.spiraclesoftware.androidsample.formatter.stringRes

class CategoryModelFormatter {

    fun format(
        category: TransactionCategory,
        isSelectedCategory: Boolean
    ): CategoryModel {
        return CategoryModel(
            ordinal = category.ordinal,
            name = category.stringRes,
            iconRes = category.drawableRes,
            iconTintRes = category.colorRes,
            isSelectedCategory = isSelectedCategory
        )
    }

}