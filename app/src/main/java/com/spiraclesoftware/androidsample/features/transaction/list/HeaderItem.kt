package com.spiraclesoftware.androidsample.features.transaction.list

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.TransactionListHeaderItemBinding
import com.spiraclesoftware.androidsample.shared.domain.Money
import com.spiraclesoftware.androidsample.shared.ui.DateTimeFormat
import org.threeten.bp.ZonedDateTime

class HeaderItem(
    private val dateTime: ZonedDateTime,
    private val balance: Money
) : AbstractBindingItem<TransactionListHeaderItemBinding>() {

    lateinit var binding: TransactionListHeaderItemBinding

    override val type: Int
        get() = R.id.transaction__list__header_item

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): TransactionListHeaderItemBinding {
        return TransactionListHeaderItemBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: TransactionListHeaderItemBinding, payloads: List<Any>) {
        this.binding = binding
        bindDateText()
        bindBalanceText()
    }

    private fun bindBalanceText() {
        binding.balanceText = balance.formatSigned(showSignWhenPositive = false)
    }

    private fun bindDateText() {
        binding.dateText = dateTime.format(DateTimeFormat.PRETTY_DATE)
    }
}