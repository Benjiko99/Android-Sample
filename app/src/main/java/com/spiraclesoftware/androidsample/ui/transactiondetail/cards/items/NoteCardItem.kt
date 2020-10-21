package com.spiraclesoftware.androidsample.ui.transactiondetail.cards.items

import android.view.LayoutInflater
import android.view.ViewGroup
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.NoteCardItemBinding
import com.spiraclesoftware.androidsample.ui.transactiondetail.cards.CardActionsHandler
import com.spiraclesoftware.core.extensions.onClick
import com.spiraclesoftware.core.extensions.string

class NoteCardItem(
    private val data: Data,
    private val actionsHandler: CardActionsHandler
) : CardItem<NoteCardItemBinding>() {

    data class Data(
        val note: String? = null
    )

    override val type = R.id.note_card_item

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?) =
        NoteCardItemBinding.inflate(inflater, parent, false)

    override fun bindView(binding: NoteCardItemBinding, payloads: List<Any>) {
        val ctx = binding.root.context

        binding.noteText = data.note
        binding.actionText = if (data.note == null)
            ctx.string(R.string.transaction__detail__note__add)
        else
            ctx.string(R.string.transaction__detail__note__edit)

        binding.actionView.onClick {
            actionsHandler.onChangeNote()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as NoteCardItem

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