package com.spiraclesoftware.androidsample.features.text_input

sealed class TextInputViewState

data class Content(
    val input: String = "",
    val error: ValidationError? = null
) : TextInputViewState()
