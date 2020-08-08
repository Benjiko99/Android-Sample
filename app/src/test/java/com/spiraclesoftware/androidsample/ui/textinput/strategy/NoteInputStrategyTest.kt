package com.spiraclesoftware.androidsample.ui.textinput.strategy

import com.spiraclesoftware.androidsample.ui.textinput.MaxLengthExceededError
import junit.framework.Assert.assertEquals
import org.junit.Test

class NoteInputStrategyTest {

    @Test
    fun `Sanitize input by trimming whitespace`() {
        val strategy = NoteInputStrategy()
        val input = "  Hello World  "

        assertEquals("Hello World", strategy.sanitizeInput(input))
    }

    @Test
    fun `Validate input without errors`() {
        val strategy = NoteInputStrategy()
        val input = "Hello World"

        assertEquals(null, strategy.validateInput(input))
    }

    @Test
    fun `Validate input exceeding max length`() {
        val strategy = NoteInputStrategy()
        val input = "Hello World, exceeding max length of input"

        assert(strategy.validateInput(input) is MaxLengthExceededError)
    }

}
