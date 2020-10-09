package com.spiraclesoftware.androidsample.ui.categoryselect

import android.view.View
import androidx.core.graphics.ColorUtils
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.domain.model.TransactionCategory
import com.spiraclesoftware.core.extensions.color
import com.spiraclesoftware.core.extensions.string
import com.spiraclesoftware.core.extensions.tintedDrawable
import kotlinx.android.synthetic.main.category__select__category_item.view.*

class CategoryItem(
    val category: TransactionCategory,
    val isChecked: Boolean
) : AbstractItem<CategoryItem.ViewHolder>() {

    override var identifier = category.ordinal.toLong()

    override val type = R.id.transaction__list__transaction_item

    override val layoutRes = R.layout.category__select__category_item

    override fun getViewHolder(v: View) = ViewHolder(v)

    class ViewHolder(val view: View) : FastAdapter.ViewHolder<CategoryItem>(view) {

        override fun bindView(item: CategoryItem, payloads: List<Any>) {
            val ctx = view.context

            view.radioView.isChecked = item.isChecked
            view.nameView.text = ctx.string(item.category.stringRes)

            fun bindCategoryIcon() {
                val tint = ctx.color(item.category.colorRes)
                view.iconView.setImageDrawable(ctx.tintedDrawable(item.category.drawableRes, tint))

                val fadedTint = ColorUtils.setAlphaComponent(tint, 255 / 100 * 15)
                view.iconView.background = ctx.tintedDrawable(R.drawable.shp_circle, fadedTint)
            }
            bindCategoryIcon()
        }

        override fun unbindView(item: CategoryItem) {
            view.nameView.text = null
            view.iconView.setImageDrawable(null)
        }

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
