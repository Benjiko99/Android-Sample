package com.spiraclesoftware.androidsample.application.extensions

import android.content.Context
import androidx.annotation.DrawableRes
import java.util.*

/**
 * Uses the [Currency]'s code to try an d find a drawable resource with the same name.
 */
@DrawableRes
fun Currency.countryImageRes(context: Context): Int {
    val resourceName = "ic_${currencyCode.toLowerCase(Locale.ROOT)}"

    return context.resources.getIdentifier(
        resourceName,
        "drawable",
        context.packageName
    )
}