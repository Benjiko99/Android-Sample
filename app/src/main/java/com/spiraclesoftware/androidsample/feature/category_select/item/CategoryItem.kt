package com.spiraclesoftware.androidsample.feature.category_select.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.CategorySelectItemBinding
import com.spiraclesoftware.androidsample.feature.category_select.item.model.CategoryModel
import com.spiraclesoftware.androidsample.framework.core.ModelAbstractBindingItem
import com.spiraclesoftware.androidsample.framework.extensions.color
import com.spiraclesoftware.androidsample.framework.extensions.string
import com.spiraclesoftware.androidsample.framework.extensions.tintedDrawable

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
        nameView.text = ctx.string(model.nameRes)

        val iconTint = ctx.color(model.iconTintRes)
        val icon = ctx.tintedDrawable(model.iconRes, iconTint)
        iconView.setImageDrawable(icon)

        val fadedTint = ColorUtils.setAlphaComponent(iconTint, 255 / 100 * 15)
        iconView.background = ctx.tintedDrawable(R.drawable.shp_circle, fadedTint)
    }

}
