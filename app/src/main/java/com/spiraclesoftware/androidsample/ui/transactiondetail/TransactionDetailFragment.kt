package com.spiraclesoftware.androidsample.ui.transactiondetail

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.graphics.ColorUtils
import androidx.core.view.isVisible
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
import com.spiraclesoftware.androidsample.domain.model.TransactionCategory
import com.spiraclesoftware.androidsample.domain.model.TransactionId
import com.spiraclesoftware.androidsample.ui.shared.DateTimeFormat
import com.spiraclesoftware.androidsample.ui.shared.DelightUI
import com.spiraclesoftware.androidsample.ui.transactiondetail.TransactionDetailViewModel.FeatureNotImplementedEvent
import com.spiraclesoftware.core.extensions.addPaintFlag
import com.spiraclesoftware.core.extensions.color
import com.spiraclesoftware.core.extensions.dpToPx
import com.spiraclesoftware.core.extensions.tintedDrawable
import io.cabriole.decorator.LinearMarginDecoration
import kotlinx.android.synthetic.main.error_with_retry.view.*
import kotlinx.android.synthetic.main.transaction__detail__fragment.*

class TransactionDetailFragment : RainbowCakeFragment<TransactionDetailViewState, TransactionDetailViewModel>() {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.transaction__detail__fragment

    private lateinit var fastAdapter: GenericFastAdapter
    private lateinit var itemAdapter: GenericItemAdapter

    override fun render(viewState: TransactionDetailViewState) {
        errorLayout.isVisible = viewState is Error

        when (viewState) {
            is DetailReady -> {
                itemAdapter.set(viewState.cardItems)

                toolbar.title = viewState.name
                nameView.text = viewState.name
                dateView.text = viewState.processingDate.format(DateTimeFormat.PRETTY_DATE_TIME)
                bindAmountText(viewState.formattedMoney, viewState.contributesToBalance)
                bindCategoryIcon(viewState.category, viewState.isSuccessful)
            }
        }
    }

    override fun onEvent(event: OneShotEvent) {
        when (event) {
            FeatureNotImplementedEvent -> {
                Toast.makeText(requireContext(), R.string.not_implemented, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun bindAmountText(formattedMoney: String, contributesToBalance: Boolean) {
        amountView.text = formattedMoney
        if (!contributesToBalance) {
            amountView.addPaintFlag(Paint.STRIKE_THRU_TEXT_FLAG)
        }
    }

    private fun bindCategoryIcon(category: TransactionCategory, isSuccessful: Boolean) {
        val tint: Int

        if (isSuccessful) {
            tint = color(category.colorRes)
            iconView.setImageDrawable(tintedDrawable(category.drawableRes, tint))
        } else {
            tint = color(R.color.transaction_status__declined)
            iconView.setImageDrawable(tintedDrawable(R.drawable.ic_status_declined, tint))
        }

        val fadedTint = ColorUtils.setAlphaComponent(tint, 255 / 100 * 15)
        iconView.background = tintedDrawable(R.drawable.shp_circle, fadedTint)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fun setupToolbar() {
            toolbar.setupWithNavController(findNavController())
            DelightUI.setupToolbarTitleAppearingOnScroll(toolbar, scrollView) {
                toolbar.height + nameView.height
            }
        }
        setupToolbar()

        fun setupFastItemAdapter() {
            itemAdapter = ItemAdapter.items()
            fastAdapter = FastAdapter.with(itemAdapter)
            // prevent the item's view from being clickable
            fastAdapter.attachDefaultListeners = false
        }
        setupFastItemAdapter()

        fun setupRecyclerView() {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
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

        errorLayout.retryButton.setOnClickListener { viewModel.retry() }
    }

    override fun onDestroyView() {
        recyclerView.adapter = null
        super.onDestroyView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            return
        }

        TransactionDetailFragmentArgs.fromBundle(requireArguments()).let { args ->
            viewModel.loadData(TransactionId(args.transactionId))
        }
    }

}
