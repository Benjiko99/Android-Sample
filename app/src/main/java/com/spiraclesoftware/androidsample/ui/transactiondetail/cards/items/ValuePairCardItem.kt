package com.spiraclesoftware.androidsample.ui.transactiondetail.cards.items

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.ValuePairCardItemBinding
import com.spiraclesoftware.androidsample.databinding.ValuePairCardItemEntryBinding
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.ui.transactiondetail.cards.CardActionsHandler
import com.spiraclesoftware.androidsample.ui.transactiondetail.cards.ValuePairCard
import com.spiraclesoftware.core.extensions.colorAttr
import com.spiraclesoftware.core.extensions.dimen
import com.spiraclesoftware.core.extensions.tintedDrawable
import com.spiraclesoftware.core.extensions.topMargin

class ValuePairCardItem(
    private val card: ValuePairCard,
    private val transaction: Transaction,
    private val actionsHandler: CardActionsHandler
) : CardItem<ValuePairCardItemBinding>() {

    data class Data(
        val label: String,
        val value: String? = null,
        val icon: Drawable? = null,
        val onClickAction: ((CardActionsHandler) -> Unit)? = null
    )

    override val type = R.id.value_pair_card_item

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ValuePairCardItemBinding {
        return ValuePairCardItemBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: ValuePairCardItemBinding, payloads: List<Any>) {
        val ctx = binding.root.context
        binding.content.removeAllViews()

        val itemData = card.toItemData(ctx, transaction)
        itemData.forEachIndexed { index, data ->
            val pb = ValuePairCardItemEntryBinding.inflate(LayoutInflater.from(ctx), binding.content, false)

            pb.labelText = data.label
            pb.valueText = data.value

            if (data.onClickAction != null) {
                pb.valueView.setOnClickListener { data.onClickAction.invoke(actionsHandler) }
                pb.valueView.isClickable = true

                val tintColor = ctx.colorAttr(R.attr.colorPrimaryDark)
                pb.valueView.setTextColor(tintColor)
                pb.iconDrawable = data.icon?.tintedDrawable(tintColor)
            } else {
                pb.valueView.setOnClickListener(null)
                pb.valueView.isClickable = false

                val tintColor = ctx.colorAttr(android.R.attr.textColorPrimary)
                pb.valueView.setTextColor(tintColor)
                pb.iconDrawable = data.icon
            }

            // Add a margin between value-pairs
            if (index > 0) pb.root.topMargin = ctx.dimen(R.dimen.content__spacing__medium)

            binding.content.addView(pb.root)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as ValuePairCardItem

        if (card != other.card) return false
        if (transaction != other.transaction) return false
        if (actionsHandler != other.actionsHandler) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + card.hashCode()
        result = 31 * result + transaction.hashCode()
        result = 31 * result + actionsHandler.hashCode()
        return result
    }

}