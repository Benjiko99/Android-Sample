package com.spiraclesoftware.androidsample.feature.transaction_detail.cards.item.model

import com.spiraclesoftware.androidsample.domain.entity.TransactionCategory
import com.spiraclesoftware.androidsample.framework.Model

data class CategoryCardModel(
    val category: TransactionCategory
) : Model
