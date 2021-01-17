package com.spiraclesoftware.androidsample.framework

import org.junit.Assert.*
import org.junit.Test

class HandleOnceValueTest {

    @Test
    fun `Is unhandled when created`() {
        val handleOnceValue = HandleOnceValue("foo")
        assertFalse(handleOnceValue.isHandled)
    }

    @Test
    fun `Is handled after value gets handled once`() {
        val handleOnceValue = HandleOnceValue("foo")

        handleOnceValue.handleOnce { }
        assertTrue(handleOnceValue.isHandled)
    }

    @Test
    fun `If was handled once before, doesn't get handled again`() {
        val handleOnceValue = HandleOnceValue("foo")
        var interactions = 0

        repeat(times = 2) {
            handleOnceValue.handleOnce { interactions++ }
        }
        assertEquals(interactions, 1)
    }

    @Test
    fun `Stored value is available in action lambda`() {
        val handleOnceValue = HandleOnceValue("foo")

        handleOnceValue.handleOnce {
            assertEquals("foo", it)
        }
    }

    @Test
    fun `Peaking returns the stored value`() {
        val handleOnceValue = HandleOnceValue("foo")
        assertEquals("foo", handleOnceValue.peak())
    }

}