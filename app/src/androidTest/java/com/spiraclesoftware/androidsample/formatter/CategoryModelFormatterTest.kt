package com.spiraclesoftware.androidsample.formatter

import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.domain.entity.TransactionCategory
import com.spiraclesoftware.androidsample.feature.category_select.CategoryModel
import com.spiraclesoftware.androidsample.feature.category_select.CategoryModelFormatter
import org.junit.Test

class CategoryModelFormatterTest : FormatterTest() {

    @Test
    fun format() {
        val category = TransactionCategory.ENTERTAINMENT
        val isSelected = true

        val expected = CategoryModel(
            ordinal = 0,
            name = R.string.transaction_category__entertainment,
            iconRes = R.drawable.ic_category_entertainment,
            iconTintRes = R.color.transaction_category__entertainment,
            isSelectedCategory = true
        )
        val actual = CategoryModelFormatter().format(category, isSelected)

        assertThat(actual).isEqualTo(expected)
    }

}