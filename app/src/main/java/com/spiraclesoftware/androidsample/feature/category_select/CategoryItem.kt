package com.spiraclesoftware.androidsample.feature.category_select

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.CategorySelectItemBinding
import com.spiraclesoftware.androidsample.extension.color
import com.spiraclesoftware.androidsample.extension.string
import com.spiraclesoftware.androidsample.extension.tintedDrawable

class CategoryItem(
    model: CategoryModel
) : ModelAbstractBindingItem<CategoryModel, CategorySelectItemBinding>(model) {

    override var identifier = model.ordinal.toLong()

    override val type = R.id.category_item

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?) =
        CategorySelectItemBinding.inflate(inflater, parent, false)

    override fun bindView(binding: CategorySelectItemBinding, payloads: List<Any>) = with(binding) {
        val ctx = root.context

        radioView.isChecked = model.isSelectedCategory
        nameView.text = ctx.string(model.name)

        val iconTint = ctx.color(model.iconTintRes)
        val icon = ctx.tintedDrawable(model.iconRes, iconTint)
        iconView.setImageDrawable(icon)

        val fadedTint = ColorUtils.setAlphaComponent(iconTint, 255 / 100 * 15)
        iconView.background = ctx.tintedDrawable(R.drawable.shp_circle, fadedTint)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as CategoryItem
        if (model != other.model) return false
        if (identifier != other.identifier) return false
        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + model.hashCode()
        result = 31 * result + identifier.hashCode()
        return result
    }

}
