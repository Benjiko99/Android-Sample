package com.spiraclesoftware.androidsample.feature.transaction_list.item

import android.view.LayoutInflater
import android.view.ViewGroup
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.TransactionHeaderItemBinding
import com.spiraclesoftware.androidsample.domain.utils.to64BitHash
import com.spiraclesoftware.androidsample.feature.transaction_list.item.model.HeaderModel
import com.spiraclesoftware.androidsample.framework.core.ModelAbstractBindingItem

class HeaderItem(
    model: HeaderModel
) : ModelAbstractBindingItem<HeaderModel, TransactionHeaderItemBinding>(model) {

    override var identifier = model.date.to64BitHash()

    override val type = R.id.transaction_header_item

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?) =
        TransactionHeaderItemBinding.inflate(inflater, parent, false)

    override fun bindView(binding: TransactionHeaderItemBinding, payloads: List<Any>) = with(binding) {
        dateView.text = model.date
        contributionView.text = model.contribution
    }

}
