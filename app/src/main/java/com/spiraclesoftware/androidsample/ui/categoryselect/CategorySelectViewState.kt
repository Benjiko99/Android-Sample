package com.spiraclesoftware.androidsample.ui.categoryselect

import com.mikepenz.fastadapter.GenericItem

sealed class CategorySelectViewState

data class CategorySelect(
    val listItems: List<GenericItem>,
    val isProcessing: Boolean = false
) : CategorySelectViewState()
