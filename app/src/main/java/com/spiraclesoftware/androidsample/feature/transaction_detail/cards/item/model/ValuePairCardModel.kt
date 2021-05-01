package com.spiraclesoftware.androidsample.feature.transaction_detail.cards.item.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.spiraclesoftware.androidsample.framework.Model

data class ValuePairCardModel(
    val valuePairModels: List<ValuePairModel>
) : Model

data class ValuePairModel(
    @StringRes val label: Int,
    val value: String,
    @DrawableRes val icon: Int? = null,
    val actionId: Int? = null
)
