package com.spiraclesoftware.core.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.listeners.CustomEventHook
import com.spiraclesoftware.core.extensions.runWithDetails

abstract class FocusEventHook<Item> : CustomEventHook<Item>() where Item : GenericItem {

    abstract fun onFocusChange(
        v: View,
        position: Int,
        adapter: FastAdapter<Item>,
        item: Item,
        hasFocus: Boolean
    )

    override fun attachEvent(view: View, viewHolder: RecyclerView.ViewHolder) {
        view.setOnFocusChangeListener { v, hasFocus ->
            runWithDetails(viewHolder) { position, adapter, item ->
                onFocusChange(v, position, adapter, item, hasFocus)
            }
        }
    }
}