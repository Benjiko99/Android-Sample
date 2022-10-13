package com.spiraclesoftware.androidsample.common.formatter

import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.R
import org.junit.Test
import org.koin.core.component.inject
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ExceptionFormatterTest : FormatterTest() {

    private val formatter by inject<ExceptionFormatter>()

    @Test
    fun format_Exception() {
        val exception = Exception()
        val actual = formatter.format(exception)
        val expected = context.getString(R.string.unknown_error)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun format_UnknownHostException() {
        val exception = UnknownHostException()
        val actual = formatter.format(exception)
        val expected = context.getString(R.string.network_error)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun format_SocketTimeoutException() {
        val exception = SocketTimeoutException()
        val actual = formatter.format(exception)
        val expected = context.getString(R.string.timeout_error)

        assertThat(actual).isEqualTo(expected)
    }

}