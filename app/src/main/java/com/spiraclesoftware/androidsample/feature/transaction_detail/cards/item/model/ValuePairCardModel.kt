package com.spiraclesoftware.androidsample.feature.transaction_detail.cards.item.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.CardActionsHandler
import com.spiraclesoftware.androidsample.framework.Model
import com.spiraclesoftware.androidsample.framework.StringHolder

data class ValuePairCardModel(
    val valuePairModels: List<ValuePairModel>
) : Model

data class ValuePairModel(
    @StringRes val label: Int,
    val value: StringHolder,
    @DrawableRes val icon: Int? = null,
    val onClickAction: ((CardActionsHandler) -> Unit)? = null
)
