package com.spiraclesoftware.androidsample.feature.transaction_list

import com.spiraclesoftware.androidsample.domain.entity.TransferDirectionFilter
import com.spiraclesoftware.androidsample.framework.Model

data class ContentModel(
    val listModels: List<Model>,
    val filterModel: FilterModel,
    val emptyState: EmptyState?,
)

data class FilterModel(
    val directionFilter: TransferDirectionFilter
)
