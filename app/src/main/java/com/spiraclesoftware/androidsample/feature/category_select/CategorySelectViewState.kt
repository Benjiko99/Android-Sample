package com.spiraclesoftware.androidsample.feature.category_select

import com.spiraclesoftware.androidsample.feature.category_select.item.model.CategoryModel

sealed class CategorySelectViewState {

    data class Content(
        val listModels: List<CategoryModel>
    ) : CategorySelectViewState()

}
