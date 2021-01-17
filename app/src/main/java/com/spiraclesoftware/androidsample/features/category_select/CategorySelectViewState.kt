package com.spiraclesoftware.androidsample.features.category_select

import com.mikepenz.fastadapter.GenericItem

sealed class CategorySelectViewState

data class Content(
    val listItems: List<GenericItem>
) : CategorySelectViewState()
