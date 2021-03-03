package com.spiraclesoftware.androidsample.feature.transaction_list

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.core.view.isVisible
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.TransactionItemBinding
import com.spiraclesoftware.androidsample.extension.*

class TransactionItem(
    model: TransactionModel
) : ModelAbstractBindingItem<TransactionModel, TransactionItemBinding>(model) {

    override var identifier = model.id.hashCode().toLong()

    override val type = R.id.transaction_item

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?) =
        TransactionItemBinding.inflate(inflater, parent, false)

    override fun bindView(binding: TransactionItemBinding, payloads: List<Any>) = with(binding) {
        val ctx = root.context
        nameView.text = model.name
        dateView.text = model.processingDate

        statusView.text = ctx.stringOrNull(model.statusCodeRes)
        statusView.isVisible = model.statusCodeRes != null

        amountView.text = model.amount

        if (!model.contributesToAccountBalance) {
            amountView.addPaintFlag(Paint.STRIKE_THRU_TEXT_FLAG)
        } else {
            amountView.removePaintFlag(Paint.STRIKE_THRU_TEXT_FLAG)
        }

        val iconTint = ctx.color(model.iconTintRes)
        val icon = ctx.tintedDrawable(model.iconRes, iconTint)
        iconView.setImageDrawable(icon)

        val fadedTint = ColorUtils.setAlphaComponent(iconTint, 255 / 100 * 15)
        iconView.background = ctx.tintedDrawable(R.drawable.shp_circle, fadedTint)
    }

    override fun equals(other: Any?): Boolean {
        if (!super.equals(other)) return false

        other as TransactionItem
        if (model != other.model) return false
        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + model.hashCode()
        return result
    }

}
