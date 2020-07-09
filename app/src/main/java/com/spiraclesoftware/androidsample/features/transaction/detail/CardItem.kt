package com.spiraclesoftware.androidsample.features.transaction.detail

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.CardItemValuePairBinding
import com.spiraclesoftware.androidsample.databinding.TransactionDetailCardItemBinding
import com.spiraclesoftware.core.extensions.colorAttr
import com.spiraclesoftware.core.extensions.dimen
import com.spiraclesoftware.core.extensions.tintedDrawable
import com.spiraclesoftware.core.extensions.topMargin

data class CardItemData(
    val label: String,
    val value: String? = null,
    val icon: Drawable? = null,
    val body: String? = null,
    val actionId: Int? = null
)

class CardItem(
    private val itemData: List<CardItemData>
) : AbstractBindingItem<TransactionDetailCardItemBinding>() {

    private lateinit var itemBinding: TransactionDetailCardItemBinding

    private var actionClickHandler: ((Int) -> Unit)? = null

    fun withActionClickHandler(func: (Int) -> Unit) {
        actionClickHandler = func
    }

    override val type: Int
        get() = R.id.transaction__detail__card_item

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): TransactionDetailCardItemBinding {
        return TransactionDetailCardItemBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: TransactionDetailCardItemBinding, payloads: List<Any>) {
        itemBinding = binding
        inflateValuePairs()
    }

    private fun inflateValuePairs() {
        itemData.forEachIndexed { index, data ->
            val ctx = itemBinding.root.context
            val binding = CardItemValuePairBinding.inflate(LayoutInflater.from(ctx), itemBinding.content, false)

            binding.labelText = data.label
            binding.valueText = data.value
            binding.bodyText = data.body

            if (data.actionId != null) {
                binding.valueView.setOnClickListener { actionClickHandler?.invoke(data.actionId) }

                val tintColor = ctx.colorAttr(R.attr.colorPrimaryDark)
                binding.valueView.setTextColor(tintColor)
                binding.iconDrawable = data.icon?.tintedDrawable(tintColor)
            } else {
                binding.iconDrawable = data.icon
            }

            // Add a margin between value-pairs
            if (index > 0) binding.root.topMargin = ctx.dimen(R.dimen.content__spacing__medium)

            itemBinding.content.addView(binding.root)
        }
    }
}
