package com.spiraclesoftware.androidsample.ui.transactionlist

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.TransactionListTransactionItemBinding
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.domain.model.TransactionStatusCode
import com.spiraclesoftware.androidsample.ui.shared.DateTimeFormat
import com.spiraclesoftware.core.extensions.color
import com.spiraclesoftware.core.extensions.string
import com.spiraclesoftware.core.extensions.tintedDrawable

class TransactionItem(val transaction: Transaction) : AbstractBindingItem<TransactionListTransactionItemBinding>() {

    override val type: Int
        get() = R.id.transaction__list__transaction_item

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): TransactionListTransactionItemBinding {
        return TransactionListTransactionItemBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: TransactionListTransactionItemBinding, payloads: List<Any>) {
        binding.nameText = transaction.name
        binding.dateText = transaction.processingDate.format(DateTimeFormat.PRETTY_DATE_TIME)

        val stringRes = transaction.statusCode.stringRes
        binding.statusText = if (stringRes != null) binding.root.context.string(stringRes) else null

        fun bindAmountText() {
            binding.amountText = transaction.formattedMoney

            if (!transaction.contributesToBalance()) {
                binding.amountView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }
        }
        bindAmountText()

        fun bindCategoryIcon() {
            val ctx = binding.root.context
            val tint: Int
            val category = transaction.category

            if (transaction.statusCode == TransactionStatusCode.SUCCESSFUL) {
                tint = ctx.color(category.colorRes)
                binding.iconDrawable = ctx.tintedDrawable(category.drawableRes, tint)
            } else {
                tint = ctx.color(R.color.transaction_status__declined)
                binding.iconDrawable = ctx.tintedDrawable(R.drawable.ic_status_declined, tint)
            }

            val fadedTint = ColorUtils.setAlphaComponent(tint, 255 / 100 * 15)
            binding.iconBgDrawable = ctx.tintedDrawable(R.drawable.shp_circle, fadedTint)
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
