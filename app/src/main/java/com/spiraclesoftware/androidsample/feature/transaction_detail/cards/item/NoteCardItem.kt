package com.spiraclesoftware.androidsample.feature.transaction_detail.cards.item

import android.view.LayoutInflater
import android.view.ViewGroup
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.NoteCardItemBinding
import com.spiraclesoftware.androidsample.extension.onClick
import com.spiraclesoftware.androidsample.extension.string
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.CardActionsHandler
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.item.model.NoteCardModel

class NoteCardItem(
    model: NoteCardModel,
    private val actionsHandler: CardActionsHandler
) : ModelBindingCardItem<NoteCardModel, NoteCardItemBinding>(model) {

    override var identifier: Long = R.id.note_card_item.toLong()

    override val type = R.id.note_card_item

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?) =
        NoteCardItemBinding.inflate(inflater, parent, false)

    override fun bindView(binding: NoteCardItemBinding, payloads: List<Any>) {
        val ctx = binding.root.context

        binding.noteText = model.note
        binding.actionText = if (model.note == null)
            ctx.string(R.string.transaction_detail__add_note)
        else
            ctx.string(R.string.transaction_detail__edit_note)

        binding.actionView.onClick {
            actionsHandler.onChangeNote()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (!super.equals(other)) return false

        other as NoteCardItem
        if (model != other.model) return false
        if (actionsHandler != other.actionsHandler) return false
        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + model.hashCode()
        result = 31 * result + actionsHandler.hashCode()
        return result
    }

}