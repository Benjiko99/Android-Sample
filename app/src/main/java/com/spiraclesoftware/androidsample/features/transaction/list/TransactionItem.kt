package com.spiraclesoftware.androidsample.features.transaction.list

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.TransactionListTransactionItemBinding
import com.spiraclesoftware.androidsample.shared.domain.Transaction
import com.spiraclesoftware.androidsample.shared.domain.TransactionStatusCode
import com.spiraclesoftware.androidsample.shared.ui.DateTimeFormat
import com.spiraclesoftware.core.extensions.color
import com.spiraclesoftware.core.extensions.string
import com.spiraclesoftware.core.extensions.tintedDrawable

class TransactionItem(val transaction: Transaction) : AbstractBindingItem<TransactionListTransactionItemBinding>() {

    lateinit var binding: TransactionListTransactionItemBinding

    override val type: Int
        get() = R.id.transaction__list__transaction_item

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): TransactionListTransactionItemBinding {
        return TransactionListTransactionItemBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: TransactionListTransactionItemBinding, payloads: List<Any>) {
        this.binding = binding
        setAmountText(transaction.formattedMoney, transaction)
        setNameText(transaction.name)
        setDateText(transaction.processingDate.format(DateTimeFormat.PRETTY_DATE_TIME))
        setStatusText(transaction.statusCode)
        setCategoryIcon(transaction.category.drawableRes, transaction.category.colorRes, transaction.statusCode)
    }

    private fun setAmountText(string: String, transaction: Transaction) {
        binding.amountText = string
        if (!transaction.contributesToBalance()) {
            binding.amountView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    private fun setNameText(string: String) {
        binding.nameText = string
    }

    private fun setDateText(string: String) {
        binding.dateText = string
    }

    private fun setStatusText(statusCode: TransactionStatusCode) {
        binding.statusText = if (statusCode.stringRes != null)
            binding.root.context.string(statusCode.stringRes!!)
        else
            null
    }

    private fun setCategoryIcon(drawableRes: Int, tintRes: Int, statusCode: TransactionStatusCode) {
        val ctx = binding.root.context
        val tint: Int

        if (statusCode == TransactionStatusCode.SUCCESSFUL) {
            tint = ctx.color(tintRes)
            binding.iconDrawable = ctx.tintedDrawable(drawableRes, tint)
        } else {
            tint = ctx.color(R.color.transaction_status__declined)
            binding.iconDrawable = ctx.tintedDrawable(R.drawable.ic_status_declined, tint)
        }

        val fadedTint = ColorUtils.setAlphaComponent(tint, 255 / 100 * 15)
        binding.iconBgDrawable = ctx.tintedDrawable(R.drawable.shp_circle, fadedTint)
    }
}
