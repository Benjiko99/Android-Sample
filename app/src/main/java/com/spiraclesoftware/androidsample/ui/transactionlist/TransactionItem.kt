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
import com.spiraclesoftware.androidsample.domain.policy.TransactionsPolicy
import com.spiraclesoftware.androidsample.ui.shared.DateTimeFormat
import com.spiraclesoftware.androidsample.ui.shared.MoneyFormat
import com.spiraclesoftware.core.extensions.*

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
            val moneyFormat = MoneyFormat(transaction.signedMoney)

            binding.amountText = moneyFormat.format(transaction)

            if (!TransactionsPolicy.contributesToBalance(transaction)) {
                binding.amountView.addPaintFlag(Paint.STRIKE_THRU_TEXT_FLAG)
            } else {
                binding.amountView.removePaintFlag(Paint.STRIKE_THRU_TEXT_FLAG)
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

}
