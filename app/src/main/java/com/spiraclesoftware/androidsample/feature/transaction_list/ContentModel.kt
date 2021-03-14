package com.spiraclesoftware.androidsample.feature.transaction_list

import com.spiraclesoftware.androidsample.framework.Model

data class ContentModel(
    val listModels: List<Model>,
    val filterModel: FilterModel,
    val emptyState: EmptyState?,
)