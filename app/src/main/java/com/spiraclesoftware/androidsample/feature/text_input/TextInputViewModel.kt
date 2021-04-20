package com.spiraclesoftware.androidsample.feature.text_input

import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.spiraclesoftware.androidsample.feature.text_input.TextInputViewState.Initial
import com.spiraclesoftware.androidsample.feature.text_input.TextInputViewState.InputEntry
import com.spiraclesoftware.androidsample.feature.text_input.strategy.TextInputStrategy

class TextInputViewModel(
    inputType: TextInputType,
    private val requestKey: String,
    initialInput: String,
    private val strategy: TextInputStrategy = TextInputStrategy.getStrategy(inputType)
) : RainbowCakeViewModel<TextInputViewState>(
    Initial(initialInput = initialInput)
) {

    data class SendResultToCallerAndExitEvent(
        val requestKey: String,
        val result: String
    ) : OneShotEvent

    val toolbarTitleRes: Int = strategy.toolbarTitleRes
    val inputHintRes: Int = strategy.inputHintRes

    init {
        viewState = InputEntry(initialInput)
    }

    fun setInput(input: String) {
        viewState = (viewState as InputEntry).copy(input = input)
    }

    fun saveInput() {
        val input = (viewState as InputEntry).input
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

    private fun updateErrorState(error: ValidationError?) {
        viewState = (viewState as InputEntry).copy(error = error)
    }

    private fun sendResultToCaller(input: String) {
        postEvent(SendResultToCallerAndExitEvent(requestKey, input))
    }

}
