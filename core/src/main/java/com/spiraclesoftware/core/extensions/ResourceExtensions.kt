package com.spiraclesoftware.core.extensions

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.drawable(@DrawableRes resId: Int): Drawable? {
    return ContextCompat.getDrawable(requireContext(), resId)
}

fun Fragment.string(@StringRes resId: Int): String {
    return getString(resId)
}

fun Fragment.string(@StringRes resId: Int, vararg formatArgs: Any): String {
    return getString(resId, *formatArgs)
}