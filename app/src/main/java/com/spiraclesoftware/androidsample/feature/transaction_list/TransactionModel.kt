package com.spiraclesoftware.androidsample.feature.transaction_list

import androidx.annotation.ColorRes
import com.spiraclesoftware.androidsample.domain.entity.TransactionId
import com.spiraclesoftware.androidsample.framework.Model

data class TransactionModel(
    val id: TransactionId,
    val amount: String,
    val name: String,
    val processingDate: String,
    val status: String?,
    val contributesToAccountBalance: Boolean,
    @ColorRes val iconRes: Int,
    @ColorRes val iconTintRes: Int
) : Model