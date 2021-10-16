package com.spiraclesoftware.androidsample.feature.category_select

import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.common.formatter.FormatterTest
import com.spiraclesoftware.androidsample.domain.entity.TransactionCategory
import com.spiraclesoftware.androidsample.feature.category_select.item.model.CategoryModel
import org.junit.Test
import org.koin.core.component.inject

class CategoryModelFormatterTest : FormatterTest() {

    private val formatter by inject<CategoryModelFormatter>()

    @Test
    fun format() {
        val category = TransactionCategory.ENTERTAINMENT
        val isSelected = true

        val expected = CategoryModel(
            ordinal = 0,
            nameRes = R.string.transaction_category__entertainment,
            iconRes = R.drawable.ic_category_entertainment,
            iconTintRes = R.color.transaction_category__entertainment,
            isSelectedCategory = true
        )
        val actual = formatter.format(category, isSelected)

        assertThat(actual).isEqualTo(expected)
    }

}