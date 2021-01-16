package com.spiraclesoftware.androidsample.utils

import android.content.Context
import android.widget.TextView
import androidx.annotation.StringRes
import com.spiraclesoftware.androidsample.extensions.string

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as StringHolder
        if (text != other.text) return false
        if (textRes != other.textRes) return false
        return true
    }

    override fun hashCode() = 31 * (text?.hashCode() ?: 0) + textRes

}

fun TextView.setText(holder: StringHolder) {
    holder.applyTo(this)
}
