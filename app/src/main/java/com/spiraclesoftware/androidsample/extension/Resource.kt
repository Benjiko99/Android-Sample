package com.spiraclesoftware.androidsample.extension

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment

//region Resources
@Px
fun Resources.dpToPx(@Dimension(unit = Dimension.DP) dp: Int): Int {
    return dpToPx(dp.toFloat()).toInt()
}

@Px
fun Resources.dpToPx(@Dimension(unit = Dimension.DP) dp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        displayMetrics
    )
}
//endregion

//region Context
fun Context.drawable(@DrawableRes resId: Int?): Drawable? {
    if (resId == null) return null
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

fun Context.string(@StringRes resId: Int, formatArgs: List<Any>?): String {
    return if (formatArgs != null) {
        string(resId, *formatArgs.toTypedArray())
    } else {
        string(resId)
    }
}

fun Context.stringOrNull(@StringRes resId: Int?): String? {
    return if (resId != null) string(resId) else null
}

@Px
fun Context.dimen(@DimenRes resId: Int): Int {
    return resources.getDimensionPixelSize(resId)
}

@Px
fun Context.dpToPx(dp: Int): Int {
    return resources.dpToPx(dp)
}

@Px
fun Context.dpToPx(dp: Float): Float {
    return resources.dpToPx(dp)
}

@ColorInt
fun Context.colorAttr(@AttrRes colorAttr: Int): Int {
    val resolvedAttr = themeAttr(colorAttr)
    // resourceId is used if it's a ColorStateList, and data if it's a color reference or a hex color
    return color(
        if (resolvedAttr.resourceId != 0)
            resolvedAttr.resourceId
        else
            resolvedAttr.data
    )
}

fun Context.themeAttr(@AttrRes attrRes: Int): TypedValue {
    val typedValue = TypedValue()
    theme.resolveAttribute(attrRes, typedValue, true)
    return typedValue
}
//endregion

//region Fragment
fun Fragment.drawable(@DrawableRes resId: Int?): Drawable? {
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

fun Fragment.string(@StringRes resId: Int, formatArgs: List<Any>?): String {
    return if (formatArgs != null) {
        string(resId, *formatArgs.toTypedArray())
    } else {
        string(resId)
    }
}

fun Fragment.stringOrNull(@StringRes resId: Int?): String? {
    return requireContext().stringOrNull(resId)
}

@Px
fun Fragment.dimen(@DimenRes resId: Int): Int {
    return requireContext().dimen(resId)
}

@Px
fun Fragment.dpToPx(dp: Int): Int {
    return resources.dpToPx(dp)
}

@Px
fun Fragment.dpToPx(dp: Float): Float {
    return resources.dpToPx(dp)
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