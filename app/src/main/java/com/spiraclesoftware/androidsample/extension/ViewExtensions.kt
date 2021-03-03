package com.spiraclesoftware.androidsample.extension

import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.TextView

//region Margins
var View.topMargin: Int?
    get() = (layoutParams as? LinearLayout.LayoutParams)?.topMargin
    set(marginPx) {
        (layoutParams as? LinearLayout.LayoutParams)?.topMargin = marginPx
    }
//endregion

//region TextView
fun TextView.addPaintFlag(flag: Int) {
    paintFlags = paintFlags or flag
}

fun TextView.removePaintFlag(flag: Int) {
    paintFlags = paintFlags and flag.inv()
}
//endregion

//region Click Listeners
fun View.onClick(func: (() -> Unit)?) {
    if (func == null) {
        setOnClickListener(null)
        isClickable = false
    } else {
        setOnClickListener { func.invoke() }
    }
}

fun View.onLongClick(func: (() -> Boolean)?) {
    if (func == null) {
        setOnLongClickListener(null)
        isLongClickable = false
    } else {
        setOnLongClickListener { func.invoke() }
    }
}
//endregion

//region Ime Action Listeners
fun TextView.onDoneAction(func: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            func()
            true
        } else false
    }
}
//endregion