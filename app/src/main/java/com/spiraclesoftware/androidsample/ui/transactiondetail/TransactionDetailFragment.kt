package com.spiraclesoftware.androidsample.ui.transactiondetail

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.graphics.ColorUtils
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.koin.getViewModelFromFactory
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericFastAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.domain.model.TransactionId
import com.spiraclesoftware.androidsample.domain.model.TransactionStatusCode
import com.spiraclesoftware.androidsample.ui.shared.DateTimeFormat
import com.spiraclesoftware.androidsample.ui.shared.DelightUI
import com.spiraclesoftware.androidsample.ui.transactiondetail.TransactionDetailViewModel.FeatureNotImplementedEvent
import com.spiraclesoftware.androidsample.ui.transactiondetail.TransactionDetailViewModel.LoadFailedEvent
import com.spiraclesoftware.androidsample.ui.transactiondetail.cards.Card
import com.spiraclesoftware.core.extensions.color
import com.spiraclesoftware.core.extensions.dpToPx
import com.spiraclesoftware.core.extensions.tintedDrawable
import io.cabriole.decorator.LinearMarginDecoration
import kotlinx.android.synthetic.main.transaction__detail__fragment.*

class TransactionDetailFragment : RainbowCakeFragment<TransactionDetailViewState, TransactionDetailViewModel>() {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.transaction__detail__fragment

    private lateinit var transactionId: TransactionId

    private lateinit var fastAdapter: GenericFastAdapter
    private lateinit var itemAdapter: GenericItemAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initArgs()

        toolbar.setupWithNavController(findNavController())
        DelightUI.setupToolbarTitleAppearingOnScroll(toolbar, scrollView)

        fun setupFastItemAdapter() {
            itemAdapter = ItemAdapter.items()
            fastAdapter = FastAdapter.with(itemAdapter)
            fastAdapter.attachDefaultListeners = false
        }
        setupFastItemAdapter()

        fun setupRecyclerView() {
            val linearLayoutManager = LinearLayoutManager(requireContext())

            recyclerView.apply {
                layoutManager = linearLayoutManager
                adapter = fastAdapter
                itemAnimator = null
                addItemDecoration(
                    LinearMarginDecoration.createVertical(
                        verticalMargin = dpToPx(16)
                    )
                )
            }
        }
        setupRecyclerView()
    }

    private fun initArgs() {
        val params = TransactionDetailFragmentArgs.fromBundle(requireArguments())
        transactionId =
            TransactionId(params.transactionId)
    }

    override fun onStart() {
        super.onStart()

        viewModel.loadTransaction(transactionId)
    }

    override fun render(viewState: TransactionDetailViewState) {
        when (viewState) {
            Loading -> {
            }
            is DetailReady -> {
                val transaction = viewState.transaction

                itemAdapter.set(viewState.cards.toListItems(transaction))

                toolbar.title = transaction.name
                nameView.text = transaction.name
                dateView.text = transaction.processingDate.format(DateTimeFormat.PRETTY_DATE_TIME)
                bindAmountText(transaction)
                bindCategoryIcon(transaction)
            }
        }
    }

    override fun onEvent(event: OneShotEvent) {
        when (event) {
            FeatureNotImplementedEvent -> {
                Toast.makeText(requireContext(), R.string.not_implemented, Toast.LENGTH_SHORT).show()
            }
            LoadFailedEvent -> {
                Toast.makeText(requireContext(), R.string.unknown_error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun bindAmountText(transaction: Transaction) {
        amountView.text = transaction.formattedMoney
        if (!transaction.contributesToBalance()) {
            amountView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    private fun bindCategoryIcon(transaction: Transaction) {
        val tint: Int
        val category = transaction.category

        if (transaction.statusCode == TransactionStatusCode.SUCCESSFUL) {
            tint = color(category.colorRes)
            iconView.setImageDrawable(tintedDrawable(category.drawableRes, tint))
        } else {
            tint = color(R.color.transaction_status__declined)
            iconView.setImageDrawable(tintedDrawable(R.drawable.ic_status_declined, tint))
        }

        val fadedTint = ColorUtils.setAlphaComponent(tint, 255 / 100 * 15)
        iconView.background = tintedDrawable(R.drawable.shp_circle, fadedTint)
    }

    private fun List<Card>.toListItems(transaction: Transaction) = map { card ->
        val itemData = card.toItemData(requireContext(), transaction)

        CardItem(itemData).apply {
            withActionClickHandler(viewModel::onCardActionClicked)
        }
    }

}
