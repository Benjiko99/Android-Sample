package com.spiraclesoftware.core.extensions

import android.view.View
import androidx.databinding.BindingAdapter

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("goneUnless")
    fun goneUnless(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("invisibleUnless")
    fun invisibleUnless(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }
}