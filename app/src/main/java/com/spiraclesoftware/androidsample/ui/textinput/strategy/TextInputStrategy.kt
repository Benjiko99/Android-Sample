package com.spiraclesoftware.androidsample.ui.textinput.strategy

import com.spiraclesoftware.androidsample.ui.textinput.ValidationError
import com.spiraclesoftware.androidsample.ui.textinput.TextInputType

interface TextInputStrategy {

    companion object {
        fun getStrategy(strategy: TextInputType) =
            when (strategy) {
                TextInputType.NOTE -> NoteInputStrategy()
            }
    }

    val toolbarTitleRes: Int
    val inputHintRes: Int

    fun validateInput(input: String): ValidationError?

    fun sanitizeInput(input: String): String

}