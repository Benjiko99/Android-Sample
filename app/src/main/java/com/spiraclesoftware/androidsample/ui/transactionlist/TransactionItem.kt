package com.spiraclesoftware.androidsample.ui.transactionlist

import android.graphics.Paint
import android.view.View
import androidx.core.graphics.ColorUtils
import androidx.core.view.isGone
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.domain.model.TransactionStatusCode
import com.spiraclesoftware.androidsample.domain.policy.TransactionsPolicy
import com.spiraclesoftware.androidsample.ui.shared.DateTimeFormat
import com.spiraclesoftware.androidsample.ui.shared.MoneyFormat
import com.spiraclesoftware.core.extensions.*
import kotlinx.android.synthetic.main.transaction__list__transaction_item.view.*

class TransactionItem(val transaction: Transaction) : AbstractItem<TransactionItem.ViewHolder>() {

    override var identifier: Long = transaction.id.value.toLong()

    override val type = R.id.transaction__list__transaction_item

    override val layoutRes = R.layout.transaction__list__transaction_item

    override fun getViewHolder(v: View) = ViewHolder(v)

    class ViewHolder(val view: View) : FastAdapter.ViewHolder<TransactionItem>(view) {

        override fun bindView(item: TransactionItem, payloads: List<Any>) {
            val ctx = view.context
            val transaction = item.transaction
            view.nameView.text = transaction.name
            view.dateView.text = transaction.processingDate.format(DateTimeFormat.PRETTY_DATE_TIME)

            view.statusView.text = view.context.stringOrNull(transaction.statusCode.stringRes).also {
                view.statusView.isGone = it == null
            }

            fun bindAmountText() {
                val moneyFormat = MoneyFormat(transaction.signedMoney)

                view.amountView.text = moneyFormat.format(transaction)

                if (!TransactionsPolicy.contributesToBalance(transaction)) {
                    view.amountView.addPaintFlag(Paint.STRIKE_THRU_TEXT_FLAG)
                } else {
                    view.amountView.removePaintFlag(Paint.STRIKE_THRU_TEXT_FLAG)
                }
            }
            bindAmountText()

            fun bindCategoryIcon() {
                val tint: Int
                val category = transaction.category

                if (transaction.statusCode == TransactionStatusCode.SUCCESSFUL) {
                    tint = ctx.color(category.colorRes)
                    view.iconView.setImageDrawable(ctx.tintedDrawable(category.drawableRes, tint))
                } else {
                    tint = ctx.color(R.color.transaction_status__declined)
                    view.iconView.setImageDrawable(ctx.tintedDrawable(R.drawable.ic_status_declined, tint))
                }

                val fadedTint = ColorUtils.setAlphaComponent(tint, 255 / 100 * 15)
                view.iconView.background = ctx.tintedDrawable(R.drawable.shp_circle, fadedTint)
            }
            bindCategoryIcon()
        }

        override fun unbindView(item: TransactionItem) {
            view.nameView.text = null
            view.dateView.text = null
            view.amountView.text = null
            view.statusView.text = null
            view.iconView.setImageDrawable(null)
        }

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
