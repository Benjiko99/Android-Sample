package com.spiraclesoftware.androidsample.feature.transaction_detail.cards.item.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.spiraclesoftware.androidsample.framework.Model

data class CategoryCardModel(
    @StringRes val nameRes: Int,
    @DrawableRes val iconRes: Int
) : Model
