package com.spiraclesoftware.androidsample.ui.textinput.strategy

import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.ui.textinput.MaxLengthExceededError
import com.spiraclesoftware.androidsample.ui.textinput.ValidationError

class NoteInputStrategy : TextInputStrategy {

    companion object {
        const val MAX_LENGTH = 32
    }

    override val toolbarTitleRes = R.string.note_input__title
    override val inputHintRes = R.string.note_input__hint

    override fun validateInput(input: String): ValidationError? {
        if (input.length > MAX_LENGTH) {
            return MaxLengthExceededError(
                R.string.note_input__error__length_exceeded,
                MAX_LENGTH
            )
        }
        return null
    }

    override fun sanitizeInput(input: String): String {
        return input.trim()
    }

}