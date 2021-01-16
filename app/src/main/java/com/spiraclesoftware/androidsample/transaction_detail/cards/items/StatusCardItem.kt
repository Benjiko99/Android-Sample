package com.spiraclesoftware.androidsample.transaction_detail.cards.items

import android.view.LayoutInflater
import android.view.ViewGroup
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.StatusCardItemBinding
import com.spiraclesoftware.androidsample.domain.model.TransactionStatus
import com.spiraclesoftware.androidsample.domain.model.TransactionStatusCode
import com.spiraclesoftware.androidsample.shared.stringRes
import com.spiraclesoftware.androidsample.extensions.string

class StatusCardItem(
    private val data: Data
) : BindingCardItem<StatusCardItemBinding>() {

    data class Data(
        val status: TransactionStatus,
        val statusCode: TransactionStatusCode
    )

    override var identifier: Long = R.id.status_card_item.toLong()

    override val type = R.id.status_card_item

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?) =
        StatusCardItemBinding.inflate(inflater, parent, false)

    override fun bindView(binding: StatusCardItemBinding, payloads: List<Any>) {
        val ctx = binding.root.context

        val status = ctx.string(data.status.stringRes)
        val statusCode = ctx.string(data.statusCode.stringRes!!)
        binding.bodyText = "$status ∙ $statusCode"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as StatusCardItem

        if (data != other.data) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + data.hashCode()
        return result
    }

}