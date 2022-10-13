package com.spiraclesoftware.androidsample.feature.transaction_detail.cards.item

import android.view.LayoutInflater
import android.view.ViewGroup
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.StatusCardItemBinding
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.item.model.StatusCardModel
import com.spiraclesoftware.androidsample.framework.extensions.string
import com.spiraclesoftware.androidsample.framework.extensions.stringOrNull

class StatusCardItem(
    model: StatusCardModel
) : ModelBindingCardItem<StatusCardModel, StatusCardItemBinding>(model) {

    override var identifier: Long = R.id.status_card_item.toLong()

    override val type = R.id.status_card_item

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?) =
        StatusCardItemBinding.inflate(inflater, parent, false)

    override fun bindView(binding: StatusCardItemBinding, payloads: List<Any>) {
        val ctx = binding.root.context

        val status = ctx.string(model.statusRes)
        val statusCode = ctx.stringOrNull(model.statusCodeRes)
        binding.bodyText = "$status ∙ $statusCode"
    }

}