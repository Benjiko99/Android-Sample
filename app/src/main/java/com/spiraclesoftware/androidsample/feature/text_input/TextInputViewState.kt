package com.spiraclesoftware.androidsample.feature.text_input

sealed class TextInputViewState {

    data class Initial(
        val initialInput: String = ""
    ) : TextInputViewState()

    data class InputEntry(
        val input: String = "",
        val error: ValidationError? = null
    ) : TextInputViewState()

}
