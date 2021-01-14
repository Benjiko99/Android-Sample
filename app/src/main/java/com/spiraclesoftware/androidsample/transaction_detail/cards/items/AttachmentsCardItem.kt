package com.spiraclesoftware.androidsample.transaction_detail.cards.items

import android.net.Uri
import android.view.View
import androidx.core.view.isGone
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.AttachmentsCardItemBinding
import com.spiraclesoftware.androidsample.transaction_detail.cards.CardActionsHandler

class AttachmentsCardItem(
    private val data: Data,
    private val actionsHandler: CardActionsHandler
) : CardItem<AttachmentsCardItem.ViewHolder>() {

    data class Data(
        val attachments: List<Uri>,
        val uploads: List<Uri>
    )

    override var identifier: Long = R.id.attachments_card_item.toLong()

    override val type = R.id.attachments_card_item

    override val layoutRes = R.layout.attachments_card_item

    override fun getViewHolder(v: View) = ViewHolder(v, actionsHandler)

    class ViewHolder(
        val view: View,
        private val actionsHandler: CardActionsHandler
    ) : FastAdapter.ViewHolder<AttachmentsCardItem>(view) {

        val binding = AttachmentsCardItemBinding.bind(view)
        private val itemAdapter = ItemAdapter.items<AttachmentsCardItemEntry>()
        private val fastAdapter = FastAdapter.with(itemAdapter)

        init {
            fastAdapter.setHasStableIds(true)

            AttachmentsCardItemEntry.addClickListener(fastAdapter, { it.photoView }) { item ->
                if (!item.isUploading)
                    actionsHandler.onViewAttachment(item.imageSource)
            }

            AttachmentsCardItemEntry.addClickListener(fastAdapter, { it.actionButton }) { item ->
                if (item.isUploading)
                    actionsHandler.onCancelUpload(item.imageSource)
                else
                    actionsHandler.onRemoveAttachment(item.imageSource)
            }
        }

        override fun bindView(item: AttachmentsCardItem, payloads: List<Any>) {
            bindRecyclerView(item)
        }

        override fun unbindView(item: AttachmentsCardItem) = with(binding) {
            recyclerView.adapter = null
        }

        private fun bindRecyclerView(item: AttachmentsCardItem) = with(binding) {
            if (recyclerView.adapter == null)
                recyclerView.adapter = fastAdapter

            val oldItemCount = itemAdapter.adapterItemCount
            val listItems = getListItems(item)
            FastAdapterDiffUtil[itemAdapter] = listItems

            // When a new item is added, scroll to the end of list
            if (listItems.size > oldItemCount)
                recyclerView.scrollToPosition(listItems.size - 1)

            recyclerView.isGone = itemAdapter.adapterItemCount == 0
        }

        private fun getListItems(item: AttachmentsCardItem): List<AttachmentsCardItemEntry> {
            val attachments = item.data.attachments.map { uri ->
                AttachmentsCardItemEntry(uri, false)
            }
            val uploads = item.data.uploads.map { uri ->
                AttachmentsCardItemEntry(uri, true)
            }
            return attachments.plus(uploads)
        }

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as AttachmentsCardItem

        if (data != other.data) return false
        if (actionsHandler != other.actionsHandler) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + data.hashCode()
        result = 31 * result + actionsHandler.hashCode()
        return result
    }

}