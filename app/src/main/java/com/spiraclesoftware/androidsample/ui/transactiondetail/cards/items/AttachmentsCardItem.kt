package com.spiraclesoftware.androidsample.ui.transactiondetail.cards.items

import android.view.LayoutInflater
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import coil.load
import com.mikepenz.fastadapter.FastAdapter
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.AttachmentsCardItemBinding
import com.spiraclesoftware.androidsample.ui.transactiondetail.cards.CardActionsHandler
import com.spiraclesoftware.core.extensions.onClick

class AttachmentsCardItem(
    private val data: Data,
    private val actionsHandler: CardActionsHandler
) : CardItem<AttachmentsCardItem.ViewHolder>() {

    data class Data(
        val attachments: List<Uri>
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

        override fun bindView(item: AttachmentsCardItem, payloads: List<Any>) = with(binding) {
            val ctx = view.context
            scrollView.isGone = data.attachments.isEmpty()
            scrollView.container.removeAllViews()

            data.attachments.forEach { url ->
                val inflater = LayoutInflater.from(ctx)
                val child = inflater.inflate(R.layout.attachments_card_item__entry, binding.scrollView.container, false)

                child.photoView.load(url)

                child.photoView.onClick {
                    actionsHandler.onViewAttachment(url)
                }

                child.removeButton.onClick {
                    actionsHandler.onRemoveAttachment(url)
                }

                scrollView.container.addView(child)
            }

            actionView.onClick {
                actionsHandler.onAddAttachment()
            }
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