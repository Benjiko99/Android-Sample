package com.spiraclesoftware.androidsample.feature.category_select

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.spiraclesoftware.androidsample.framework.Model

data class CategoryModel(
    val ordinal: Int,
    @StringRes val nameRes: Int,
    @ColorRes val iconRes: Int,
    @ColorRes val iconTintRes: Int,
    val isSelectedCategory: Boolean
) : Model
