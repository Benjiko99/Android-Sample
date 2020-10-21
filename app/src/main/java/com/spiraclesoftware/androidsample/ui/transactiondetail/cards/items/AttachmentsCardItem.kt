package com.spiraclesoftware.androidsample.ui.transactiondetail.cards.items

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import coil.load
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.AttachmentsCardItemBinding
import com.spiraclesoftware.androidsample.ui.transactiondetail.cards.CardActionsHandler
import com.spiraclesoftware.core.extensions.onClick
import kotlinx.android.synthetic.main.attachments_card_item.view.*
import kotlinx.android.synthetic.main.attachments_card_item__entry.view.*

class AttachmentsCardItem(
    private val data: Data,
    private val actionsHandler: CardActionsHandler
) : CardItem<AttachmentsCardItemBinding>() {

    data class Data(
        val attachments: List<String>
    )

    override val type = R.id.attachments_card_item

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?) =
        AttachmentsCardItemBinding.inflate(inflater, parent, false)

    override fun bindView(binding: AttachmentsCardItemBinding, payloads: List<Any>) {
        val ctx = binding.root.context
        binding.scrollView.isGone = data.attachments.isEmpty()
        binding.scrollView.container.removeAllViews()

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

            binding.scrollView.container.addView(child)
        }

        binding.actionView.onClick {
            actionsHandler.onAddAttachment()
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