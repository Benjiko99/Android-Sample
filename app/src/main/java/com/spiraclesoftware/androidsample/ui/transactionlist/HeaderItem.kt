package com.spiraclesoftware.androidsample.ui.transactionlist

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.TransactionListHeaderItemBinding
import com.spiraclesoftware.androidsample.domain.model.Money
import com.spiraclesoftware.androidsample.ui.shared.DateTimeFormat
import com.spiraclesoftware.androidsample.ui.shared.MoneyFormat
import org.threeten.bp.ZonedDateTime

class HeaderItem(
    private val dateTime: ZonedDateTime,
    private val contributionToBalance: Money
) : AbstractBindingItem<TransactionListHeaderItemBinding>() {

    override val type: Int
        get() = R.id.transaction__list__header_item

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): TransactionListHeaderItemBinding {
        return TransactionListHeaderItemBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: TransactionListHeaderItemBinding, payloads: List<Any>) {
        val formattedContribution = MoneyFormat(contributionToBalance)
            .formatSigned(showSignWhenPositive = false)

        binding.contributionsText = formattedContribution
        binding.dateText = dateTime.format(DateTimeFormat.PRETTY_DATE)
    }

}
