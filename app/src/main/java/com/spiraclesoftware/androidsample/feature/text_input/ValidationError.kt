package com.spiraclesoftware.androidsample.feature.text_input

import android.content.Context
import com.spiraclesoftware.androidsample.framework.extensions.string

open class ValidationError(
    private val stringRes: Int,
    private val stringFormatArgs: List<Any>? = null
) {

    fun formattedMessage(ctx: Context): String {
        return ctx.string(stringRes, stringFormatArgs)
    }

}

class MaxLengthExceededError(stringRes: Int, maxLength: Int) :
    ValidationError(stringRes, listOf(maxLength))
