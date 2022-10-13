package com.spiraclesoftware.androidsample.feature.transaction_detail.cards.item

import android.view.LayoutInflater
import android.view.ViewGroup
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.CategoryCardItemBinding
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.item.model.CategoryCardModel
import com.spiraclesoftware.androidsample.framework.extensions.drawable
import com.spiraclesoftware.androidsample.framework.extensions.onClick
import com.spiraclesoftware.androidsample.framework.extensions.string

class CategoryCardItem(
    model: CategoryCardModel,
    private val onSelectCategory: () -> Unit
) : ModelBindingCardItem<CategoryCardModel, CategoryCardItemBinding>(model) {

    override var identifier: Long = R.id.category_card_item.toLong()

    override val type = R.id.category_card_item

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?) =
        CategoryCardItemBinding.inflate(inflater, parent, false)

    override fun bindView(binding: CategoryCardItemBinding, payloads: List<Any>) {
        val ctx = binding.root.context
        binding.categoryText = ctx.string(model.nameRes)
        binding.categoryIcon = ctx.drawable(model.iconRes)

        binding.actionView.onClick(::onSelectCategory.get())
    }

}