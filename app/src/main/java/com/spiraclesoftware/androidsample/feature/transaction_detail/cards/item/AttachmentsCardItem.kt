package com.spiraclesoftware.androidsample.feature.transaction_detail.cards.item

import android.net.Uri
import android.view.View
import androidx.core.view.isGone
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.AttachmentsCardItemBinding
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.item.model.AttachmentsCardModel
import com.spiraclesoftware.androidsample.framework.extensions.onClick

class AttachmentsCardItem(
    model: AttachmentsCardModel,
    private val actionListener: ActionListener
) : ModelCardItem<AttachmentsCardModel, AttachmentsCardItem.ViewHolder>(model) {

    interface ActionListener {
        fun onAddAttachment()
        fun onViewAttachment(uri: Uri)
        fun onRemoveAttachment(uri: Uri)
        fun onCancelUpload(uri: Uri)
    }

    override var identifier: Long = R.id.attachments_card_item.toLong()

    override val type = R.id.attachments_card_item

    override val layoutRes = R.layout.attachments_card_item

    override fun getViewHolder(v: View) = ViewHolder(v)

    class ViewHolder(val view: View) : FastAdapter.ViewHolder<AttachmentsCardItem>(view) {

        val binding = AttachmentsCardItemBinding.bind(view)
        private val itemAdapter = ItemAdapter.items<AttachmentsCardItemEntry>()
        private val fastAdapter = FastAdapter.with(itemAdapter)

        init {
            fastAdapter.setHasStableIds(true)
        }

        override fun bindView(item: AttachmentsCardItem, payloads: List<Any>) {
            bindRecyclerView(item)
            binding.actionView.onClick(item.actionListener::onAddAttachment)
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
            val attachments = item.model.attachments.map { uri ->
                AttachmentsCardItemEntry(uri, false, item.actionListener)
            }
            val uploads = item.model.uploads.map { uri ->
                AttachmentsCardItemEntry(uri, true, item.actionListener)
            }
            return attachments.plus(uploads)
        }

    }

}