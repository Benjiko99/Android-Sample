package com.spiraclesoftware.androidsample.feature.transaction_detail.cards.items

import android.view.LayoutInflater
import android.view.ViewGroup
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.CategoryCardItemBinding
import com.spiraclesoftware.androidsample.domain.entity.TransactionCategory
import com.spiraclesoftware.androidsample.extension.drawable
import com.spiraclesoftware.androidsample.extension.onClick
import com.spiraclesoftware.androidsample.extension.string
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.CardActionsHandler
import com.spiraclesoftware.androidsample.formatter.drawableRes
import com.spiraclesoftware.androidsample.formatter.stringRes

class CategoryCardItem(
    private val data: Data,
    private val actionsHandler: CardActionsHandler
) : BindingCardItem<CategoryCardItemBinding>() {

    data class Data(
        val category: TransactionCategory
    )

    override var identifier: Long = R.id.category_card_item.toLong()

    override val type = R.id.category_card_item

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?) =
        CategoryCardItemBinding.inflate(inflater, parent, false)

    override fun bindView(binding: CategoryCardItemBinding, payloads: List<Any>) {
        val ctx = binding.root.context
        binding.categoryText = ctx.string(data.category.stringRes)
        binding.categoryIcon = ctx.drawable(data.category.drawableRes)

        binding.actionView.onClick {
            actionsHandler.onSelectCategory()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as CategoryCardItem

        if (data != other.data) return false
        if (actionsHandler != other.actionsHandler) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + data.hashCode()
        result = 31 * result + actionsHandler.hashCode()
        return result
    }

}