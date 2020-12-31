package com.spiraclesoftware.androidsample.ui.transactionlist

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.core.view.isGone
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.TransactionItemBinding
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.domain.model.TransactionStatusCode
import com.spiraclesoftware.androidsample.ui.shared.DateTimeFormat
import com.spiraclesoftware.androidsample.ui.shared.MoneyFormat
import com.spiraclesoftware.core.extensions.*

class TransactionItem(
    val transaction: Transaction
) : AbstractBindingItem<TransactionItemBinding>() {

    override var identifier: Long = transaction.id.value.toLong()

    override val type = R.id.transaction_item

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?) =
        TransactionItemBinding.inflate(inflater, parent, false)

    override fun bindView(binding: TransactionItemBinding, payloads: List<Any>) = with(binding) {
        val ctx = root.context
        val transaction = transaction
        nameView.text = transaction.name
        dateView.text = transaction.processingDate.format(DateTimeFormat.PRETTY_DATE_TIME)

        statusView.text = ctx.stringOrNull(transaction.statusCode.stringRes).also {
            statusView.isGone = it == null
        }

        fun bindAmountText() {
            val moneyFormat = MoneyFormat(transaction.signedMoney)

            amountView.text = moneyFormat.format(transaction)

            if (!transaction.contributesToAccountBalance()) {
                amountView.addPaintFlag(Paint.STRIKE_THRU_TEXT_FLAG)
            } else {
                amountView.removePaintFlag(Paint.STRIKE_THRU_TEXT_FLAG)
            }
        }
        bindAmountText()

        fun bindCategoryIcon() {
            val tint: Int
            val category = transaction.category

            if (transaction.statusCode == TransactionStatusCode.SUCCESSFUL) {
                tint = ctx.color(category.colorRes)
                iconView.setImageDrawable(ctx.tintedDrawable(category.drawableRes, tint))
            } else {
                tint = ctx.color(R.color.transaction_status__declined)
                iconView.setImageDrawable(ctx.tintedDrawable(R.drawable.ic_status_declined, tint))
            }

            val fadedTint = ColorUtils.setAlphaComponent(tint, 255 / 100 * 15)
            iconView.background = ctx.tintedDrawable(R.drawable.shp_circle, fadedTint)
        }
        bindCategoryIcon()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as TransactionItem
        if (transaction != other.transaction) return false
        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + transaction.hashCode()
        return result
    }

}
