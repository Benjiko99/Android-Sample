package com.spiraclesoftware.androidsample.framework

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class HandleOnceValueTest {

    @Test
    fun `Is unhandled when created`() {
        val handleOnceValue = HandleOnceValue("foo")
        assertThat(handleOnceValue.isHandled).isFalse()
    }

    @Test
    fun `Is handled after value gets handled once`() {
        val handleOnceValue = HandleOnceValue("foo")

        handleOnceValue.handleOnce { }
        assertThat(handleOnceValue.isHandled).isTrue()
    }

    @Test
    fun `If was handled once before, doesn't get handled again`() {
        val handleOnceValue = HandleOnceValue("foo")
        var interactions = 0

        repeat(times = 2) {
            handleOnceValue.handleOnce { interactions++ }
        }
        assertThat(interactions).isEqualTo(1)
    }

    @Test
    fun `Stored value is available in action lambda`() {
        val handleOnceValue = HandleOnceValue("foo")

        handleOnceValue.handleOnce {
            assertThat(it).isEqualTo("foo")
        }
    }

    @Test
    fun `Peaking returns the stored value`() {
        val handleOnceValue = HandleOnceValue("foo")
        assertThat(handleOnceValue.peak()).isEqualTo("foo")
    }

}