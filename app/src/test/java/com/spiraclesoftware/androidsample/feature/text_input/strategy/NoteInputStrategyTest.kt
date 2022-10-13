package com.spiraclesoftware.androidsample.feature.text_input.strategy

import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.feature.text_input.MaxLengthExceededError
import org.junit.Test

class NoteInputStrategyTest {

    @Test
    fun `Sanitize input by trimming whitespace`() {
        val strategy = NoteInputStrategy()
        val input = "  Hello World  "

        assertThat(strategy.sanitizeInput(input)).isEqualTo("Hello World")
    }

    @Test
    fun `Validate input without errors`() {
        val strategy = NoteInputStrategy()
        val input = "Hello World"

        assertThat(strategy.validateInput(input)).isNull()
    }

    @Test
    fun `Validate input exceeding max length`() {
        val strategy = NoteInputStrategy()
        val input = "Hello World, exceeding max length of input"

        assertThat(strategy.validateInput(input)).isInstanceOf(MaxLengthExceededError::class.java)
    }

}
