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

    override val type = R.id.transaction__detail__card_item

    private var actionClickHandler: ((Int) -> Unit)? = null

    fun withActionClickHandler(func: (Int) -> Unit) {
        actionClickHandler = func
    }

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): TransactionDetailCardItemBinding {
        return TransactionDetailCardItemBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: TransactionDetailCardItemBinding, payloads: List<Any>) {
        val ctx = binding.root.context
        binding.content.removeAllViews()

        val itemData = card.toItemData(ctx, transaction)
        itemData.forEachIndexed { index, data ->
            val pb = CardItemValuePairBinding.inflate(LayoutInflater.from(ctx), binding.content, false)

            pb.labelText = data.label
            pb.valueText = data.value
            pb.bodyText = data.body

            if (data.actionId != null) {
                val onClick: (View) -> Unit = { actionClickHandler?.invoke(data.actionId) }
                pb.valueView.setOnClickListener(onClick)
                pb.valueView.isClickable = true

                val tintColor = ctx.colorAttr(R.attr.colorPrimaryDark)
                pb.valueView.setTextColor(tintColor)
                pb.iconDrawable = data.icon?.tintedDrawable(tintColor)
            } else {
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
