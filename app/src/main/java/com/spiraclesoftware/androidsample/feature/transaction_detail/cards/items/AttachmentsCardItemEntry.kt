package com.spiraclesoftware.androidsample.feature.transaction_detail.cards.items

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import coil.load
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.binding.listeners.addClickListener
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.AttachmentsCardItemEntryBinding

class AttachmentsCardItemEntry(
    val imageSource: Uri,
    val isUploading: Boolean
) : BindingCardItem<AttachmentsCardItemEntryBinding>() {

    // Uses the URI of the image as the identifier
    override var identifier: Long = imageSource.toString().hashCode().toLong()

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
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
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

    companion object {
        inline fun <reified Item : GenericItem> addClickListener(
            adapter: FastAdapter<Item>,
            crossinline resolveView: (AttachmentsCardItemEntryBinding) -> View?,
            crossinline onClick: (AttachmentsCardItemEntry) -> Unit
        ) {
            adapter.addClickListener(resolveView) { _, _, _, entry ->
                if (entry is AttachmentsCardItemEntry) onClick(entry)
            }
        }
    }

}