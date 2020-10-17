package com.spiraclesoftware.androidsample.ui.transactiondetail.cards.items

import android.view.LayoutInflater
import android.view.ViewGroup
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.StatusCardItemBinding
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.domain.model.TransactionStatus
import com.spiraclesoftware.androidsample.domain.model.TransactionStatusCode
import com.spiraclesoftware.androidsample.ui.transactiondetail.cards.StatusCard
import com.spiraclesoftware.core.extensions.string

class StatusCardItem(
    private val card: StatusCard,
    private val transaction: Transaction
) : CardItem<StatusCardItemBinding>() {

    data class Data(
        val status: TransactionStatus,
        val statusCode: TransactionStatusCode
    )

    override val type = R.id.status_card_item

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?) =
        StatusCardItemBinding.inflate(inflater, parent, false)

    override fun bindView(binding: StatusCardItemBinding, payloads: List<Any>) {
        val ctx = binding.root.context
        val data = card.toItemData(transaction)
        binding.bodyText = ctx.string(data.status.stringRes) + " ∙ " + ctx.string(data.statusCode.stringRes!!)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as StatusCardItem

        if (card != other.card) return false
        if (transaction != other.transaction) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + card.hashCode()
        result = 31 * result + transaction.hashCode()
        return result
    }

}