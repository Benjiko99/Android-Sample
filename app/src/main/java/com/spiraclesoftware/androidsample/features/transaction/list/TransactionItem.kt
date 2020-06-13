package com.spiraclesoftware.androidsample.features.transaction.list

import android.graphics.Paint
import android.view.View
import androidx.core.graphics.ColorUtils
import androidx.databinding.DataBindingUtil
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.TransactionListTransactionItemBinding
import com.spiraclesoftware.androidsample.shared.domain.Transaction
import com.spiraclesoftware.androidsample.shared.domain.TransactionStatusCode
import com.spiraclesoftware.androidsample.shared.ui.DateTimeFormat
import com.spiraclesoftware.core.extensions.color
import com.spiraclesoftware.core.extensions.string
import com.spiraclesoftware.core.extensions.tintedDrawable
import kotlinx.android.synthetic.main.transaction__list__transaction_item.view.*

class TransactionItem(val transaction: Transaction) : AbstractItem<TransactionItem, TransactionItem.ViewHolder>() {

    override fun getType(): Int = R.id.transaction__list__transaction_item

    override fun getLayoutRes(): Int = R.layout.transaction__list__transaction_item

    override fun getViewHolder(view: View): ViewHolder = ViewHolder(view)

    class ViewHolder(val view: View) : FastAdapter.ViewHolder<TransactionItem>(view) {

        private val binding = DataBindingUtil.getBinding(view) ?: TransactionListTransactionItemBinding.bind(view)

        override fun bindView(item: TransactionItem, payloads: List<Any>) {
            val transaction = item.transaction
            setAmountText(transaction.formattedMoney, transaction.statusCode)
            setNameText(transaction.name)
            setDateText(transaction.processingDate.format(DateTimeFormat.PRETTY_DATE_TIME))
            setStatusText(transaction.statusCode)
            setCategoryIcon(transaction.category.drawableRes, transaction.category.colorRes, transaction.statusCode)
        }

        override fun unbindView(item: TransactionItem) {
            view.nameView.text = null
            view.dateView.text = null
            view.statusView.text = null
            view.amountView.text = null
            view.iconView.setImageDrawable(null)
            view.iconView.background = null
        }

        private fun setAmountText(string: String, statusCode: TransactionStatusCode) {
            binding.amountText = string
            if (statusCode != TransactionStatusCode.SUCCESSFUL) {
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
                view.context.string(statusCode.stringRes!!)
            else
                null
        }

        private fun setCategoryIcon(drawableRes: Int, tintRes: Int, statusCode: TransactionStatusCode) {
            val ctx = view.context
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
}
