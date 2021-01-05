package com.spiraclesoftware.androidsample.ui.categoryselect

import com.mikepenz.fastadapter.GenericItem

sealed class CategorySelectViewState

data class Content(
    val listItems: List<GenericItem>
) : CategorySelectViewState()
