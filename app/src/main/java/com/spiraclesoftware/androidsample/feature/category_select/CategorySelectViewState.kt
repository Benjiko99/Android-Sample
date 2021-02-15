package com.spiraclesoftware.androidsample.feature.category_select

sealed class CategorySelectViewState {

    data class Content(
        val listModels: List<CategoryModel>
    ) : CategorySelectViewState()

}
