package com.spiraclesoftware.airbankinterview.transaction.list.ui

import android.view.View
import androidx.core.widget.TextViewCompat
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.spiraclesoftware.airbankinterview.R
import com.spiraclesoftware.airbankinterview.databinding.TransactionListTransactionItemBinding
import com.spiraclesoftware.airbankinterview.transaction.list.domain.TransactionId
import com.spiraclesoftware.airbankinterview.transaction.shared.domain.TransactionDirection
import kotlinx.android.synthetic.main.transaction__list__transaction_item.view.*

class TransactionItem : AbstractItem<TransactionItem, TransactionItem.ViewHolder>() {

    var transactionId: TransactionId = 0
        private set
    private var transactionAmount: Int = 0
    private lateinit var transactionDirection: TransactionDirection

    fun withTransactionId(id: TransactionId): TransactionItem {
        this.transactionId = id
        return this
    }

    fun withTransactionAmount(amount: Int): TransactionItem {
        this.transactionAmount = amount
        return this
    }

    fun withTransactionDirection(direction: TransactionDirection): TransactionItem {
        this.transactionDirection = direction
        return this
    }

    override fun getType(): Int = R.id.transaction__list__transaction_item

    override fun getLayoutRes(): Int = R.layout.transaction__list__transaction_item

    override fun getViewHolder(view: View): ViewHolder = ViewHolder(view)

    class ViewHolder(val view: View) : FastAdapter.ViewHolder<TransactionItem>(view) {

        override fun bindView(item: TransactionItem, payloads: List<Any>) {
            val context = view.context
            val binding = TransactionListTransactionItemBinding.bind(view)

            binding.apply {
                transactionAmount = item.transactionAmount.toString()
                transactionDirection = context.getString(item.transactionDirection.getStringRes())
                iconView.setImageResource(item.transactionDirection.getDrawableRes())

                val transactionAmountTextAppearance = when (item.transactionDirection) {
                    TransactionDirection.INCOMING -> R.style.TextAppearance_Transaction_Amount_Incoming
                    TransactionDirection.OUTGOING -> R.style.TextAppearance_Transaction_Amount_Outgoing
                }
                TextViewCompat.setTextAppearance(transactionAmountView, transactionAmountTextAppearance)
            }
        }

        override fun unbindView(item: TransactionItem) {
            view.transactionAmountView.text = null
            view.transactionDirectionView.text = null
            view.iconView.setImageDrawable(null)
        }
    }
}
