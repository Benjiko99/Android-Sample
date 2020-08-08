package com.spiraclesoftware.androidsample.ui.transactiondetail.cards

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.CardItemValuePairBinding
import com.spiraclesoftware.androidsample.databinding.TransactionDetailCardItemBinding
import com.spiraclesoftware.androidsample.domain.model.Transaction
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

fun Card.toItemData(ctx: Context, transaction: Transaction): List<CardItemData> {
    return valuePairs.map {
        it.toItemData(ctx, transaction)
    }
}

class CardItem(
    private val card: Card,
    private val transaction: Transaction
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
        val ctx = itemBinding.root.context
        itemBinding.content.removeAllViews()

        val itemData = card.toItemData(ctx, transaction)
        itemData.forEachIndexed { index, data ->
            val binding = CardItemValuePairBinding.inflate(LayoutInflater.from(ctx), itemBinding.content, false)

            binding.labelText = data.label
            binding.valueText = data.value
            binding.bodyText = data.body

            if (data.actionId != null) {
                val onClick: (View) -> Unit = { actionClickHandler?.invoke(data.actionId) }
                binding.valueView.setOnClickListener(onClick)
                binding.valueView.isClickable = true

                val tintColor = ctx.colorAttr(R.attr.colorPrimaryDark)
                binding.valueView.setTextColor(tintColor)
                binding.iconDrawable = data.icon?.tintedDrawable(tintColor)
            } else {
                binding.valueView.isClickable = false

                val tintColor = ctx.colorAttr(android.R.attr.textColorPrimary)
                binding.valueView.setTextColor(tintColor)
                binding.iconDrawable = data.icon
            }

            // Add a margin between value-pairs
            if (index > 0) binding.root.topMargin = ctx.dimen(R.dimen.content__spacing__medium)

            itemBinding.content.addView(binding.root)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as CardItem

        if (card != other.card) return false
        if (transaction != other.transaction) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + card.hashCode()
        result = 31 * result + transaction.hashCode()
        return result
    }

}
