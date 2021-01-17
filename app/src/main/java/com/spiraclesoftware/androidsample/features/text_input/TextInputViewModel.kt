package com.spiraclesoftware.androidsample.features.text_input

import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.spiraclesoftware.androidsample.features.text_input.strategy.TextInputStrategy

class TextInputViewModel(
    inputType: TextInputType,
    private val requestKey: String,
    initialValue: String,
    private val strategy: TextInputStrategy = TextInputStrategy.getStrategy(inputType)
) : RainbowCakeViewModel<TextInputViewState>(
    Content(input = initialValue)
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
        viewState = (viewState as? Content)?.copy(
            input = input
        ) ?: viewState
    }

    private fun updateErrorState(error: ValidationError?) {
        viewState = (viewState as? Content)?.copy(
            error = error
        ) ?: viewState
    }

    private fun sendResultToCaller(input: String) {
        postEvent(SendResultToCallerAndExitEvent(requestKey, input))
    }

}
