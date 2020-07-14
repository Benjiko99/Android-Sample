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

    override fun render(viewState: TransactionDetailViewState) {
        when (viewState) {
            Loading -> {
            }
            is DetailReady -> {
                val transaction = viewState.transaction

                itemAdapter.set(viewState.cardItems)

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

    // TODO: The logic for whether a view is shown and what business values it displays
    //  should be moved to the ViewModel or Presenter, it needs to be in the viewState...
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        TransactionDetailFragmentArgs.fromBundle(requireArguments()).let { args ->
            transactionId = TransactionId(args.transactionId)
        }

        fun setupToolbar() {
            toolbar.setupWithNavController(findNavController())
            DelightUI.setupToolbarTitleAppearingOnScroll(toolbar, scrollView)
        }
        setupToolbar()

        fun setupFastItemAdapter() {
            itemAdapter = ItemAdapter.items()
            fastAdapter = FastAdapter.with(itemAdapter)
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
    }

    override fun onStart() {
        super.onStart()

        viewModel.loadData(transactionId)
    }

}
