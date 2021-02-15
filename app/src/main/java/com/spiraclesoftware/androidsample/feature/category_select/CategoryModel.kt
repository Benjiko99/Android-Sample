package com.spiraclesoftware.androidsample.feature.category_select

import androidx.annotation.ColorRes
import com.spiraclesoftware.androidsample.framework.Model

data class CategoryModel(
    val ordinal: Int,
    val name: String,
    @ColorRes val iconRes: Int,
    @ColorRes val iconTintRes: Int,
    val isSelectedCategory: Boolean
) : Model
