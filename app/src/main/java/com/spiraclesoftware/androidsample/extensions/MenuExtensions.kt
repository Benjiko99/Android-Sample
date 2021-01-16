package com.spiraclesoftware.androidsample.extensions

import android.view.MenuItem

fun MenuItem.onActionExpanded(onExpanded: (isExpanded: Boolean) -> Unit) {
    setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
        override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
            onExpanded(true)
            return true
        }

        override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
            onExpanded(false)
            return true
        }
    })
}
