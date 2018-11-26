package com.spiraclesoftware.core.utils

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.DrawableCompat

object ResourceUtils {

    /** NOTE: You need to use the returned drawable! */
    fun getTintedDrawable(drawable: Drawable, @ColorInt tintColor: Int): Drawable {
        return DrawableCompat.wrap(drawable).apply {
            setTint(tintColor)
        }
    }
}