package com.spiraclesoftware.core.extensions

import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IItem
import com.mikepenz.fastadapter.listeners.CustomEventHook

//region EventHook
/**
 * Utility that handles getting the details needed by the event like:
 * the [FastAdapter], the [IItem] and the item's position in the adapter.
 */
fun <Item : IItem<*, *>> CustomEventHook<Item>.runWithDetails(
    viewHolder: RecyclerView.ViewHolder,
    func: (position: Int, adapter: FastAdapter<Item>, item: Item) -> Unit
) {
    val adapter = getFastAdapter(viewHolder)!!
    val pos = adapter.getHolderAdapterPosition(viewHolder)
    if (pos != RecyclerView.NO_POSITION) {
        val item = adapter.getItem(pos)
        if (item != null) {
            func(pos, adapter, item)
        }
    }
}
//endregion
