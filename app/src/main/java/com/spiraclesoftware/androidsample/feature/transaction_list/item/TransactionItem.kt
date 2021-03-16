package com.spiraclesoftware.androidsample.feature.transaction_list.item

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.core.view.isVisible
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.TransactionItemBinding
import com.spiraclesoftware.androidsample.domain.util.to64BitHash
import com.spiraclesoftware.androidsample.extension.*
import com.spiraclesoftware.androidsample.feature.transaction_list.item.model.TransactionModel
import com.spiraclesoftware.androidsample.framework.ModelAbstractBindingItem

class TransactionItem(
    model: TransactionModel
) : ModelAbstractBindingItem<TransactionModel, TransactionItemBinding>(model) {

    override var identifier = model.id.value.to64BitHash()

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

}
