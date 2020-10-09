package com.spiraclesoftware.androidsample.ui.transactionlist

import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.domain.model.Money
import com.spiraclesoftware.androidsample.ui.shared.DateTimeFormat
import com.spiraclesoftware.androidsample.ui.shared.MoneyFormat
import kotlinx.android.synthetic.main.transaction__list__header_item.view.*
import org.threeten.bp.ZonedDateTime

class HeaderItem(
    private val dateTime: ZonedDateTime,
    private val contributionToBalance: Money
) : AbstractItem<HeaderItem.ViewHolder>() {

    override var identifier: Long = hashCode().toLong()

    override val type = R.id.transaction__list__header_item

    override val layoutRes = R.layout.transaction__list__header_item

    override fun getViewHolder(v: View) = ViewHolder(v)

    class ViewHolder(val view: View) : FastAdapter.ViewHolder<HeaderItem>(view) {

        override fun bindView(item: HeaderItem, payloads: List<Any>) {
            view.dateView.text = item.dateTime.format(DateTimeFormat.PRETTY_DATE)
            view.contributionsView.text = MoneyFormat(item.contributionToBalance)
                .formatSigned(showSignWhenPositive = false)
        }

        override fun unbindView(item: HeaderItem) {
            view.dateView.text = null
            view.contributionsView.text = null
        }

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as HeaderItem
        if (dateTime != other.dateTime) return false
        if (contributionToBalance != other.contributionToBalance) return false
        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + dateTime.hashCode()
        result = 31 * result + contributionToBalance.hashCode()
        return result
    }

}
