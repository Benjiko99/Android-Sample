package com.spiraclesoftware.androidsample.features.transaction_detail.cards.items

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.ValuePairCardItemBinding
import com.spiraclesoftware.androidsample.databinding.ValuePairCardItemEntryBinding
import com.spiraclesoftware.androidsample.extensions.*
import com.spiraclesoftware.androidsample.features.transaction_detail.cards.CardActionsHandler
import com.spiraclesoftware.androidsample.framework.StringHolder

class ValuePairCardItem(
    private val data: List<Data>,
    private val actionsHandler: CardActionsHandler
) : BindingCardItem<ValuePairCardItemBinding>() {

    data class Data(
        @StringRes val label: Int,
        val value: StringHolder,
        @DrawableRes val icon: Int? = null,
        val onClickAction: ((CardActionsHandler) -> Unit)? = null
    )

    override var identifier: Long = R.id.value_pair_card_item.toLong()

    override val type = R.id.value_pair_card_item

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?) =
        ValuePairCardItemBinding.inflate(inflater, parent, false)

    override fun bindView(binding: ValuePairCardItemBinding, payloads: List<Any>) {
        val ctx = binding.root.context
        binding.content.removeAllViews()

        data.forEachIndexed { index, data ->
            val pb = ValuePairCardItemEntryBinding.inflate(LayoutInflater.from(ctx), binding.content, false)

            pb.labelText = ctx.string(data.label)
            pb.valueText = data.value.getString(ctx)

            if (data.onClickAction != null) {
                pb.valueView.onClick { data.onClickAction.invoke(actionsHandler) }

                val tintColor = ctx.colorAttr(R.attr.colorPrimary)
                pb.valueView.setTextColor(tintColor)
                pb.iconDrawable = ctx.drawable(data.icon)?.tintedDrawable(tintColor)
            } else {
                pb.valueView.onClick(null)

                val tintColor = ctx.colorAttr(android.R.attr.textColorPrimary)
                pb.valueView.setTextColor(tintColor)
                pb.iconDrawable = ctx.drawable(data.icon)
            }

            // Add a margin between value-pairs
            if (index > 0) pb.root.topMargin = ctx.dimen(R.dimen.content_spacing_2x)

            binding.content.addView(pb.root)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as ValuePairCardItem

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