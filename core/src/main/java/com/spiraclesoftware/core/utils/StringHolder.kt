package com.spiraclesoftware.core.utils

import android.content.Context
import android.widget.TextView
import androidx.annotation.StringRes
import com.spiraclesoftware.core.extensions.string

/**
 * Supports providing strings either as String or resource ID.
 */
class StringHolder private constructor(
    private val text: String?,
    private val textRes: Int
) {

    constructor(text: String?) : this(text, -1)

    constructor(@StringRes textRes: Int) : this(null, textRes)

    fun getString(ctx: Context): String? {
        return if (textRes != -1)
            ctx.string(textRes)
        else text
    }

    fun applyTo(textView: TextView?) {
        if (textRes != -1)
            textView?.setText(textRes)
        else
            textView?.text = text
    }

}

fun TextView.setText(holder: StringHolder) {
    holder.applyTo(this)
}
