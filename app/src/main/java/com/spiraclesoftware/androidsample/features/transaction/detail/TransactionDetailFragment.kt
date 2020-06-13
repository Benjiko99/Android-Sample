package com.spiraclesoftware.androidsample.features.transaction.detail

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.TransactionDetailDetailItemBinding
import com.spiraclesoftware.androidsample.databinding.TransactionDetailFragmentBinding
import com.spiraclesoftware.androidsample.shared.domain.Transaction
import com.spiraclesoftware.androidsample.shared.domain.TransactionId
import com.spiraclesoftware.androidsample.shared.domain.TransactionStatus
import com.spiraclesoftware.androidsample.shared.domain.TransactionStatusCode
import com.spiraclesoftware.androidsample.shared.ui.DateTimeFormat
import com.spiraclesoftware.androidsample.shared.ui.DelightUI
import com.spiraclesoftware.core.data.Resource
import com.spiraclesoftware.core.extensions.color
import com.spiraclesoftware.core.extensions.drawable
import com.spiraclesoftware.core.extensions.string
import com.spiraclesoftware.core.extensions.tintedDrawable
import kotlinx.android.synthetic.main.transaction__detail__detail_card.view.*
import kotlinx.android.synthetic.main.transaction__detail__fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransactionDetailFragment : Fragment() {

    private val viewModel by viewModel<TransactionDetailViewModel>()

    private lateinit var binding: TransactionDetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TransactionDetailFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setupWithNavController(findNavController())

        DelightUI.setupToolbarTitleAppearingOnScroll(toolbar, scrollView)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val params = TransactionDetailFragmentArgs.fromBundle(requireArguments())

        viewModel.setTransactionId(TransactionId(params.transactionId))

        fun subscribeUi() {
            viewModel.transaction.observe(
                viewLifecycleOwner,
                TransactionResourceObserver()
            )
        }
        subscribeUi()
    }

    inner class TransactionResourceObserver : Observer<Resource<Transaction>> {

        override fun onChanged(resource: Resource<Transaction>?) {
            (resource?.data)?.let { transaction ->
                toolbar.title = transaction.name

                setAmountText(transaction.formattedMoney)
                setNameText(transaction.name)
                setDateText(transaction.processingDate.format(DateTimeFormat.PRETTY_DATE_TIME))
                setCategoryIcon(transaction.category.drawableRes, transaction.category.colorRes)

                populateCards(transaction)
            }
        }
    }

    private fun setAmountText(string: String) {
        binding.amountText = string
    }

    private fun setNameText(string: String) {
        binding.nameText = string
    }

    private fun setDateText(string: String) {
        binding.dateText = string
    }

    private fun setCategoryIcon(drawableRes: Int, tintRes: Int) {
        val tint = color(tintRes)
        val fadedTint = ColorUtils.setAlphaComponent(tint, 255 / 100 * 15)

        binding.iconDrawable = tintedDrawable(drawableRes, tint)
        binding.iconBgDrawable = tintedDrawable(R.drawable.shp_circle, fadedTint)
    }

    private fun populateCards(transaction: Transaction) {
        val statusCard = arrayListOf<CardItem>()
        val infoCard = arrayListOf<CardItem>()
        val noteCard = arrayListOf<CardItem>()

        val cards = arrayListOf(
            statusCard, infoCard, noteCard
        )

        if (transaction.statusCode != TransactionStatusCode.SUCCESSFUL) {
            statusCard.add(
                CardItem(
                    label = string(R.string.transaction__detail__status),
                    body = "${string(transaction.status.stringRes)} ∙ ${string(transaction.statusCode.stringRes!!)}"
                )
            )
        } else {
            infoCard.add(
                CardItem(
                    label = string(R.string.transaction__detail__status),
                    value = string(transaction.status.stringRes)
                )
            )
        }

        if (!transaction.cardDescription.isNullOrEmpty()) {
            infoCard.add(
                CardItem(
                    label = string(R.string.transaction__detail__card),
                    value = transaction.cardDescription.orEmpty(),
                    icon = drawable(R.drawable.ic_credit_card),
                    action = {
                        Toast.makeText(requireContext(), R.string.not_implemented, Toast.LENGTH_SHORT).show()
                    }
                )
            )
        }

        if (transaction.status == TransactionStatus.COMPLETED) {
            infoCard.add(
                CardItem(
                    label = string(R.string.transaction__detail__statement),
                    value = string(R.string.transaction__detail__download),
                    icon = drawable(R.drawable.ic_download_statement),
                    action = {
                        Toast.makeText(requireContext(), R.string.not_implemented, Toast.LENGTH_SHORT).show()
                    }
                )
            )
        }

        noteCard.add(
            CardItem(
                label = string(R.string.transaction__detail__note),
                value = if (transaction.noteToSelf == null)
                    string(R.string.transaction__detail__note__add)
                else
                    string(R.string.transaction__detail__note__edit),
                body = transaction.noteToSelf,
                icon = drawable(R.drawable.ic_edit_note),
                action = {
                    Toast.makeText(requireContext(), R.string.not_implemented, Toast.LENGTH_SHORT).show()
                }
            )
        )

        CardsUtils(requireContext(), layoutInflater, cardsContainer).makeCards(cards)
    }

    class CardsUtils(
        private val ctx: Context,
        private val layoutInflater: LayoutInflater,
        private val cardsContainer: ViewGroup
    ) {
        fun makeCards(cards: ArrayList<ArrayList<CardItem>>) {
            cardsContainer.removeAllViews()

            cards.filterNot { it.isEmpty() }.forEachIndexed { cardIndex, items ->

                val cardView = layoutInflater.inflate(R.layout.transaction__detail__detail_card, cardsContainer, false)

                items.forEachIndexed { itemIndex, item ->
                    val itemBinding =
                        TransactionDetailDetailItemBinding.inflate(layoutInflater, cardView.content, false)
                    itemBinding.labelText = item.label
                    itemBinding.valueText = item.value
                    itemBinding.bodyText = item.body

                    if (item.action != null) {
                        itemBinding.valueView.setOnClickListener { item.action.invoke() }
                        itemBinding.valueView.setTextColor(ctx.color(R.color.blue_700))
                        itemBinding.iconDrawable = item.icon?.tintedDrawable(ctx.color(R.color.blue_700))
                    } else {
                        itemBinding.iconDrawable = item.icon
                    }

                    // Add a margin between items
                    val layoutParams = itemBinding.root.layoutParams as LinearLayout.LayoutParams
                    if (itemIndex > 0) {
                        layoutParams.topMargin = ctx.resources.getDimensionPixelSize(R.dimen.content__spacing__medium)
                    }

                    cardView.content.addView(itemBinding.root, layoutParams)
                }

                // Add a margin between cards
                val layoutParams = cardView.layoutParams as LinearLayout.LayoutParams
                if (cardIndex > 0) {
                    layoutParams.topMargin = ctx.resources.getDimensionPixelSize(R.dimen.content__spacing__medium)
                }

                cardsContainer.addView(cardView, layoutParams)
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
}