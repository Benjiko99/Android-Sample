package com.spiraclesoftware.core.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment

//region Context
fun Context.drawable(@DrawableRes resId: Int): Drawable? {
    return ContextCompat.getDrawable(this, resId)
}

fun Context.tintedDrawable(@DrawableRes resId: Int, @ColorInt tint: Int): Drawable? {
    return drawable(resId)?.tintedDrawable(tint)
}

@ColorInt
fun Context.color(@ColorRes resId: Int): Int {
    return ContextCompat.getColor(this, resId)
}

fun Context.string(@StringRes resId: Int): String {
    return getString(resId)
}

fun Context.string(@StringRes resId: Int, vararg formatArgs: Any): String {
    return getString(resId, *formatArgs)
}
//endregion

//region Fragment
fun Fragment.drawable(@DrawableRes resId: Int): Drawable? {
    return requireContext().drawable(resId)
}

fun Fragment.tintedDrawable(@DrawableRes resId: Int, @ColorInt tint: Int): Drawable? {
    return requireContext().tintedDrawable(resId, tint)
}

@ColorInt
fun Fragment.color(@ColorRes resId: Int): Int {
    return requireContext().color(resId)
}

fun Fragment.string(@StringRes resId: Int): String {
    return requireContext().string(resId)
}

fun Fragment.string(@StringRes resId: Int, vararg formatArgs: Any): String {
    return requireContext().string(resId, *formatArgs)
}
//endregion

//region Drawable
/** NOTE: You need to use the returned drawable! */
fun Drawable.tintedDrawable(@ColorInt tintColor: Int): Drawable {
    return DrawableCompat.wrap(this.mutate()).apply {
        setTint(tintColor)
    }
}
//endregion