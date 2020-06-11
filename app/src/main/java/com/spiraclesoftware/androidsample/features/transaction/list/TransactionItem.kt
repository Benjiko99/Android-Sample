package com.spiraclesoftware.androidsample.features.transaction.list

import android.view.View
import androidx.core.graphics.ColorUtils
import androidx.databinding.DataBindingUtil
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.TransactionListTransactionItemBinding
import com.spiraclesoftware.androidsample.shared.domain.Transaction
import com.spiraclesoftware.androidsample.shared.ui.DateTimeFormat
import com.spiraclesoftware.core.extensions.color
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
            setAmountText(transaction.formattedMoney)
            setNameText(transaction.name)
            setDateText(transaction.processingDate.format(DateTimeFormat.PRETTY_DATE_TIME))
            setCategoryIcon(transaction.category.drawableRes, transaction.category.colorRes)
        }

        override fun unbindView(item: TransactionItem) {
            view.nameView.text = null
            view.dateView.text = null
            view.amountView.text = null
            view.iconView.setImageDrawable(null)
            view.iconView.background = null
        }

        private fun setAmountText(string: String) {
            binding.amountText = string
        }

        private fun setNameText(string: String) {
            binding.nameText = string
        }

        private fun setDateText(string: String) {
            binding.dateText = string
        }

        private fun setCategoryIcon(drawableRes: Int, tintRes: Int) {
            val ctx = view.context
            val tint = ctx.color(tintRes)
            val fadedTint = ColorUtils.setAlphaComponent(tint, 255 / 100 * 15)

            binding.iconDrawable = ctx.tintedDrawable(drawableRes, tint)
            binding.iconBgDrawable = ctx.tintedDrawable(R.drawable.shp_circle, fadedTint)
        }
    }
}
