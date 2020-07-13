package com.spiraclesoftware.core.extensions

import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

val Toolbar.titleView: TextView?
    get() {
        return when {
            // If there's a navigation icon, then title will be the second view
            this.getChildAt(1) is TextView -> this.getChildAt(1)
            // If there's no navigation icon, then title will be the first view
            this.getChildAt(0) is TextView -> this.getChildAt(0)
            // If no title is set, then the title view is not present in the layout
            else -> null
        } as TextView?
    }

fun AdapterView<*>.onItemSelected(
    onNothingSelected: ((AdapterView<*>) -> Unit)? = null,
    onItemSelected: (AdapterView<*>, View?, Int, Long) -> Unit
) {
    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

        override fun onNothingSelected(parent: AdapterView<*>) {
            onNothingSelected?.invoke(parent)
        }

        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            onItemSelected.invoke(parent, view, position, id)
        }

    }
}