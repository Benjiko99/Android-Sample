package com.spiraclesoftware.androidsample.features.text_input.strategy

import com.spiraclesoftware.androidsample.features.text_input.TextInputType
import com.spiraclesoftware.androidsample.features.text_input.ValidationError

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