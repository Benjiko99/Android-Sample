package com.spiraclesoftware.androidsample.features.transaction.list

import android.view.View
import androidx.core.widget.TextViewCompat
import androidx.databinding.DataBindingUtil
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.TransactionListTransactionItemBinding
import com.spiraclesoftware.androidsample.shared.domain.Transaction
import com.spiraclesoftware.androidsample.shared.domain.TransactionDirection
import kotlinx.android.synthetic.main.transaction__list__transaction_item.view.*

class TransactionItem(val transaction: Transaction) : AbstractItem<TransactionItem, TransactionItem.ViewHolder>() {

    override fun getType(): Int = R.id.transaction__list__transaction_item

    override fun getLayoutRes(): Int = R.layout.transaction__list__transaction_item

    override fun getViewHolder(view: View): ViewHolder = ViewHolder(view)

    class ViewHolder(val view: View) : FastAdapter.ViewHolder<TransactionItem>(view) {

        override fun bindView(item: TransactionItem, payloads: List<Any>) {
            val binding = DataBindingUtil.getBinding(view) ?: TransactionListTransactionItemBinding.bind(view)

            binding.transaction = item.transaction
            binding.apply {
                fun setAmountTextAppearance() {
                    val transactionAmountTextAppearance = when (item.transaction.direction) {
                        TransactionDirection.INCOMING -> R.style.TextAppearance_Transaction_Amount_Incoming
                        TransactionDirection.OUTGOING -> R.style.TextAppearance_Transaction_Amount_Outgoing
                    }
                    TextViewCompat.setTextAppearance(transactionAmountView, transactionAmountTextAppearance)
                }
                setAmountTextAppearance()
            }
        }

        override fun unbindView(item: TransactionItem) {
            view.transactionAmountView.text = null
            view.transactionDirectionView.text = null
            view.iconView.setImageDrawable(null)
        }
    }
}
