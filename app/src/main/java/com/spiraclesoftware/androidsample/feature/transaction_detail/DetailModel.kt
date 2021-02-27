package com.spiraclesoftware.androidsample.feature.transaction_detail

import androidx.annotation.ColorRes
import com.spiraclesoftware.androidsample.domain.entity.Transaction
import com.spiraclesoftware.androidsample.domain.entity.TransactionId
import com.spiraclesoftware.androidsample.framework.Model

data class DetailModel(
    val transaction: Transaction,
    val id: TransactionId,
    val name: String,
    val formattedMoney: String,
    val processingDate: String,
    @ColorRes val iconRes: Int,
    @ColorRes val iconTintRes: Int,
    val contributesToBalance: Boolean,
    val isSuccessful: Boolean
) : Model