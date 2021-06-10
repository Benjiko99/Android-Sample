package com.spiraclesoftware.androidsample.framework.extensions

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.IBinder
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import androidx.fragment.app.Fragment

fun Fragment.showSoftKeyboard(view: View) {
    requireContext().showSoftKeyboard(view)
}

fun Fragment.hideSoftKeyboard() {
    val windowToken = requireView().rootView.windowToken
    requireContext().hideSoftKeyboard(windowToken)
}

/**
 * @param view focus will be given to this view
 */
fun Context.showSoftKeyboard(view: View) {
    val hasFocus = view.requestFocus()
    if (hasFocus) inputMethodManager().showSoftInput(view, SHOW_IMPLICIT)
}

/**
 * @param windowToken can be retrieved from any [View] with view.windowToken
 */
fun Context.hideSoftKeyboard(windowToken: IBinder) {
    inputMethodManager().hideSoftInputFromWindow(windowToken, 0)
}

private fun Context.inputMethodManager() =
    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
