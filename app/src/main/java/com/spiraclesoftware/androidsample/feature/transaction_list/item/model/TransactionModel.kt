package com.spiraclesoftware.androidsample.feature.transaction_list.item.model

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.spiraclesoftware.androidsample.domain.entity.TransactionId
import com.spiraclesoftware.androidsample.framework.core.Model

data class TransactionModel(
    val id: TransactionId,
    val amount: String,
    val name: String,
    val processingDate: String,
    @StringRes val statusCodeRes: Int?,
    val contributesToAccountBalance: Boolean,
    @DrawableRes val iconRes: Int,
    @ColorRes val iconTintRes: Int
) : Model