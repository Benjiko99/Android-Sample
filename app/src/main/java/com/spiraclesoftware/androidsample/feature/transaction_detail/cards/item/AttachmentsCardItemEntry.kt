package com.spiraclesoftware.androidsample.feature.transaction_detail.cards.item

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import coil.load
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.AttachmentsCardItemEntryBinding
import com.spiraclesoftware.androidsample.domain.utils.to64BitHash
import com.spiraclesoftware.androidsample.framework.extensions.onClick

class AttachmentsCardItemEntry(
    val imageSource: Uri,
    val isUploading: Boolean,
    private val actionListener: AttachmentsCardItem.ActionListener
) : AbstractBindingItem<AttachmentsCardItemEntryBinding>() {

    override var identifier = imageSource.toString().to64BitHash()

    override val type = R.id.attachments_card_item_entry

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?) =
        AttachmentsCardItemEntryBinding.inflate(inflater, parent, false)

    override fun bindView(binding: AttachmentsCardItemEntryBinding, payloads: List<Any>) {
        binding.photoView.load(imageSource) {
            error(R.drawable.ic_image_error)
            placeholder(R.drawable.ic_image_placeholder)
        }

        binding.progressBar.isVisible = isUploading

        val actionIcon = if (isUploading) R.drawable.ic_cancel_upload else R.drawable.ic_remove_photo
        binding.actionButton.setImageResource(actionIcon)

        binding.photoView.onClick {
            if (!isUploading)
                actionListener.onViewAttachment(imageSource)
        }

        binding.actionButton.onClick {
            if (isUploading)
                actionListener.onCancelUpload(imageSource)
            else
                actionListener.onRemoveAttachment(imageSource)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (!super.equals(other)) return false

        other as AttachmentsCardItemEntry
        if (imageSource != other.imageSource) return false
        if (isUploading != other.isUploading) return false
        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + imageSource.hashCode()
        result = 31 * result + isUploading.hashCode()
        return result
    }

}