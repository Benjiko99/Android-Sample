package com.spiraclesoftware.androidsample.ui.textinput

import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.spiraclesoftware.androidsample.ui.textinput.strategy.TextInputStrategy

class TextInputViewModel(
    inputType: TextInputType,
    private val requestKey: String,
    initialValue: String,
    private val strategy: TextInputStrategy = TextInputStrategy.getStrategy(inputType)
) : RainbowCakeViewModel<TextInputViewState>(
    TextInputEntry(input = initialValue)
) {

    data class SendResultToCallerAndExitEvent(
        val requestKey: String,
        val result: String
    ) : OneShotEvent

    val toolbarTitleRes: Int = strategy.toolbarTitleRes
    val inputHintRes: Int = strategy.inputHintRes

    fun validateThenSendInputToCaller(input: String) {
        updateInputState(input)

        val sanitizedInput = strategy.sanitizeInput(input)

        if (validateInput(sanitizedInput)) {
            sendResultToCaller(sanitizedInput)
        }
    }

    private fun validateInput(input: String): Boolean {
        val error = strategy.validateInput(input).also {
            updateErrorState(it)
        }
        return error == null
    }

    private fun updateInputState(input: String) {
        viewState = (viewState as? TextInputEntry)?.copy(
            input = input
        ) ?: viewState
    }

    private fun updateErrorState(error: ValidationError?) {
        viewState = (viewState as? TextInputEntry)?.copy(
            error = error
        ) ?: viewState
    }

    private fun sendResultToCaller(input: String) {
        postEvent(SendResultToCallerAndExitEvent(requestKey, input))
    }

}
