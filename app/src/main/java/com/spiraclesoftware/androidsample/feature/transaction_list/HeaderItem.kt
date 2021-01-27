package com.spiraclesoftware.androidsample.feature.transaction_list

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.TransactionHeaderItemBinding
import com.spiraclesoftware.androidsample.domain.entity.Money
import com.spiraclesoftware.androidsample.formatter.DateTimeFormat
import com.spiraclesoftware.androidsample.formatter.MoneyFormat
import org.threeten.bp.ZonedDateTime

class HeaderItem(
    private val dateTime: ZonedDateTime,
    private val contributionToBalance: Money
) : AbstractBindingItem<TransactionHeaderItemBinding>() {

    override var identifier: Long = hashCode().toLong()

    override val type = R.id.transaction_header_item

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?) =
        TransactionHeaderItemBinding.inflate(inflater, parent, false)

    override fun bindView(binding: TransactionHeaderItemBinding, payloads: List<Any>) = with(binding) {
        dateView.text = dateTime.format(DateTimeFormat.PRETTY_DATE)
        contributionsView.text = MoneyFormat(contributionToBalance)
            .formatSigned(showSignWhenPositive = false)
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
