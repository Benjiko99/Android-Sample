package com.spiraclesoftware.androidsample.feature.transaction_detail.cards.item.model

import androidx.annotation.StringRes
import com.spiraclesoftware.androidsample.framework.core.Model

data class StatusCardModel(
    @StringRes val statusRes: Int,
    @StringRes val statusCodeRes: Int?
) : Model
