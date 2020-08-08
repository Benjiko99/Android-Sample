package com.spiraclesoftware.androidsample.ui.textinput

sealed class TextInputViewState

data class TextInputEntry(
    val input: String = "",
    val error: ValidationError? = null
) : TextInputViewState()
