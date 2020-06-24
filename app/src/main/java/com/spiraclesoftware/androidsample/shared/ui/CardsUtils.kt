package com.spiraclesoftware.androidsample.shared.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.TransactionDetailCardItemBinding
import com.spiraclesoftware.core.extensions.colorAttr
import com.spiraclesoftware.core.extensions.dimen
import com.spiraclesoftware.core.extensions.tintedDrawable
import com.spiraclesoftware.core.extensions.topMargin
import kotlinx.android.synthetic.main.transaction__detail__card.view.*

class CardsUtils(
    private val ctx: Context,
    private val layoutInflater: LayoutInflater,
    private val cardsContainer: ViewGroup
) {
    fun makeCards(cards: ArrayList<ArrayList<CardItem>>) {
        cardsContainer.removeAllViews()

        cards.filterNot { it.isEmpty() }.forEachIndexed { cardIndex, items ->

            val cardView = layoutInflater.inflate(R.layout.transaction__detail__card, cardsContainer, false)

            items.forEachIndexed { itemIndex, item ->
                val itemBinding = TransactionDetailCardItemBinding.inflate(layoutInflater, cardView.content, false)
                itemBinding.labelText = item.label
                itemBinding.valueText = item.value
                itemBinding.bodyText = item.body

                if (item.action != null) {
                    itemBinding.valueView.setOnClickListener { item.action.invoke() }

                    val tintColor = ctx.colorAttr(R.attr.colorPrimaryDark)
                    itemBinding.valueView.setTextColor(tintColor)
                    itemBinding.iconDrawable = item.icon?.tintedDrawable(tintColor)
                } else {
                    itemBinding.iconDrawable = item.icon
                }

                // Add a margin between items
                if (itemIndex > 0) {
                    itemBinding.root.topMargin = ctx.dimen(R.dimen.content__spacing__medium)
                }

                cardView.content.addView(itemBinding.root)
            }

            // Add a margin between cards
            if (cardIndex > 0) {
                cardView.topMargin = ctx.dimen(R.dimen.content__spacing__medium)
            }

            cardsContainer.addView(cardView)
        }
    }
}

data class CardItem(
    val label: String,
    val value: String? = null,
    val icon: Drawable? = null,
    val body: String? = null,
    val action: (() -> Unit)? = null
)