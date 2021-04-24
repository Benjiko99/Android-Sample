package com.spiraclesoftware.androidsample.format

import android.content.Context
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.extension.string
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ExceptionFormatter(private val ctx: Context) {

    fun format(exception: Throwable) = when (exception) {
        is SocketTimeoutException -> ctx.string(R.string.timeout_error)
        is UnknownHostException -> ctx.string(R.string.network_error)
        else -> ctx.string(R.string.unknown_error)
    }

}