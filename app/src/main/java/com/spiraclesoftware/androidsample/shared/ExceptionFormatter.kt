package com.spiraclesoftware.androidsample.shared

import android.content.Context
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.core.extensions.string
//import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ExceptionFormatter(private val ctx: Context) {

    fun format(exception: Exception) = when (exception) {
        is SocketTimeoutException -> ctx.string(R.string.timeout_error)

//      TODO:  is HttpException,

        is UnknownHostException -> ctx.string(R.string.network_error)
        else -> ctx.string(R.string.unknown_error)
    }

}