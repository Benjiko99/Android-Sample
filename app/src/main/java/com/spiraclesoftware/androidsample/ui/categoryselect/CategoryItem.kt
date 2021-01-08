package com.spiraclesoftware.androidsample.ui.categoryselect

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.CategorySelectItemBinding
import com.spiraclesoftware.androidsample.domain.model.TransactionCategory
import com.spiraclesoftware.androidsample.ui.shared.colorRes
import com.spiraclesoftware.androidsample.ui.shared.drawableRes
import com.spiraclesoftware.androidsample.ui.shared.stringRes
import com.spiraclesoftware.core.extensions.color
import com.spiraclesoftware.core.extensions.string
import com.spiraclesoftware.core.extensions.tintedDrawable

class CategoryItem(
    val category: TransactionCategory,
    val isChecked: Boolean
) : AbstractBindingItem<CategorySelectItemBinding>() {

    override var identifier = category.ordinal.toLong()

    override val type = R.id.transaction_item

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?) =
        CategorySelectItemBinding.inflate(inflater, parent, false)

    override fun bindView(binding: CategorySelectItemBinding, payloads: List<Any>) = with(binding) {
        val ctx = root.context

        radioView.isChecked = isChecked
        nameView.text = ctx.string(category.stringRes)

        fun bindCategoryIcon() {
            val tint = ctx.color(category.colorRes)
            iconView.setImageDrawable(ctx.tintedDrawable(category.drawableRes, tint))

            val fadedTint = ColorUtils.setAlphaComponent(tint, 255 / 100 * 15)
            iconView.background = ctx.tintedDrawable(R.drawable.shp_circle, fadedTint)
        }
        bindCategoryIcon()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as CategoryItem
        if (category != other.category) return false
        if (isChecked != other.isChecked) return false
        if (identifier != other.identifier) return false
        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + category.hashCode()
        result = 31 * result + isChecked.hashCode()
        result = 31 * result + identifier.hashCode()
        return result
    }

}
