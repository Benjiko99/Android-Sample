package com.spiraclesoftware.androidsample.feature.transaction_detail.cards.item

import android.view.LayoutInflater
import android.view.ViewGroup
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.ValuePairCardItemBinding
import com.spiraclesoftware.androidsample.databinding.ValuePairCardItemEntryBinding
import com.spiraclesoftware.androidsample.extension.*
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.CardActionsHandler
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.item.model.ValuePairCardModel

class ValuePairCardItem(
    model: ValuePairCardModel,
    private val actionsHandler: CardActionsHandler
) : ModelBindingCardItem<ValuePairCardModel, ValuePairCardItemBinding>(model) {

    override var identifier: Long = R.id.value_pair_card_item.toLong()

    override val type = R.id.value_pair_card_item

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?) =
        ValuePairCardItemBinding.inflate(inflater, parent, false)

    override fun bindView(binding: ValuePairCardItemBinding, payloads: List<Any>) {
        val ctx = binding.root.context
        binding.content.removeAllViews()

        model.valuePairModels.forEachIndexed { index, model ->
            val pb = ValuePairCardItemEntryBinding.inflate(LayoutInflater.from(ctx), binding.content, false)

            pb.labelText = ctx.string(model.label)
            pb.valueText = model.value.getString(ctx)

            if (model.onClickAction != null) {
                pb.valueView.onClick { model.onClickAction.invoke(actionsHandler) }

                val tintColor = ctx.colorAttr(R.attr.colorPrimary)
                pb.valueView.setTextColor(tintColor)
                pb.iconDrawable = ctx.drawable(model.icon)?.tintedDrawable(tintColor)
            } else {
                pb.valueView.onClick(null)

                val tintColor = ctx.colorAttr(android.R.attr.textColorPrimary)
                pb.valueView.setTextColor(tintColor)
                pb.iconDrawable = ctx.drawable(model.icon)
            }

            // Add a margin between value-pairs
            if (index > 0) pb.root.topMargin = ctx.dimen(R.dimen.content_spacing_2x)

            binding.content.addView(pb.root)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (!super.equals(other)) return false

        other as ValuePairCardItem
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