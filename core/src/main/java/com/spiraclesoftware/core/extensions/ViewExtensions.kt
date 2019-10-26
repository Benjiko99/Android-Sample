package com.spiraclesoftware.core.extensions

import android.view.View
import android.widget.LinearLayout

//region Margins
var View.topMargin: Int?
    get() = (layoutParams as? LinearLayout.LayoutParams)?.topMargin
    set(marginPx) {
        (layoutParams as? LinearLayout.LayoutParams)?.topMargin = marginPx
    }

var View.rightMargin: Int?
    get() = (layoutParams as? LinearLayout.LayoutParams)?.rightMargin
    set(marginPx) {
        (layoutParams as? LinearLayout.LayoutParams)?.rightMargin = marginPx
    }

var View.bottomMargin: Int?
    get() = (layoutParams as? LinearLayout.LayoutParams)?.bottomMargin
    set(marginPx) {
        (layoutParams as? LinearLayout.LayoutParams)?.bottomMargin = marginPx
    }

var View.leftMargin: Int?
    get() = (layoutParams as? LinearLayout.LayoutParams)?.leftMargin
    set(marginPx) {
        (layoutParams as? LinearLayout.LayoutParams)?.leftMargin = marginPx
    }
//endregion