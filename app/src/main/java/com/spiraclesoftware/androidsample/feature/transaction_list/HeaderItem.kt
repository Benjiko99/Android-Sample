package com.spiraclesoftware.androidsample.feature.transaction_list

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.TransactionHeaderItemBinding

class HeaderItem(
    model: HeaderModel
) : ModelAbstractBindingItem<HeaderModel, TransactionHeaderItemBinding>(model) {

    override var identifier: Long = hashCode().toLong()

    override val type = R.id.transaction_header_item

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?) =
        TransactionHeaderItemBinding.inflate(inflater, parent, false)

    override fun bindView(binding: TransactionHeaderItemBinding, payloads: List<Any>) = with(binding) {
        dateView.text = model.date
        contributionView.text = model.contribution
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as HeaderItem
        if (model != other.model) return false
        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + model.hashCode()
        return result
    }

}
