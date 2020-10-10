package com.spiraclesoftware.androidsample.ui.transactiondetail

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.graphics.ColorUtils
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericFastAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.domain.model.TransactionCategory
import com.spiraclesoftware.androidsample.domain.model.TransactionId
import com.spiraclesoftware.androidsample.ui.shared.DateTimeFormat
import com.spiraclesoftware.androidsample.ui.shared.DelightUI
import com.spiraclesoftware.androidsample.ui.textinput.TextInputFragment
import com.spiraclesoftware.androidsample.ui.transactiondetail.TransactionDetailViewModel.*
import com.spiraclesoftware.core.extensions.*
import io.cabriole.decorator.LinearMarginDecoration
import kotlinx.android.synthetic.main.transaction__detail__fragment.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class TransactionDetailFragment : RainbowCakeFragment<TransactionDetailViewState, TransactionDetailViewModel>() {

    companion object {
        const val NOTE_INPUT_REQUEST_KEY = "noteInputRequest"
    }

    override fun provideViewModel(): TransactionDetailViewModel {
        TransactionDetailFragmentArgs.fromBundle(requireArguments()).let { args ->
            val id = TransactionId(args.transactionId)
            return getViewModel { parametersOf(id) }
        }
    }

    override fun getViewResource() = R.layout.transaction__detail__fragment

    private lateinit var fastAdapter: GenericFastAdapter
    private lateinit var itemAdapter: GenericItemAdapter

    override fun render(viewState: TransactionDetailViewState) {
        errorMessageView.isVisible = viewState is Error

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
            is NavigateToNoteInputEvent -> {
                findNavController().navigate(event.navDirections)
            }
            is NavigateToCategorySelectEvent -> {
                findNavController().navigate(event.navDirections)
            }
            is NavigateToCardDetailEvent -> {
                showToast(R.string.not_implemented, Toast.LENGTH_SHORT)
            }
            is DownloadStatementEvent -> {
                showToast(R.string.not_implemented, Toast.LENGTH_SHORT)
            }
            is OpenAttachmentPickerEvent -> {
                showToast(R.string.not_implemented, Toast.LENGTH_SHORT)
            }
            is NotifyOfFailureEvent -> {
                showToast(event.stringRes, Toast.LENGTH_LONG)
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
    }

    override fun onDestroyView() {
        recyclerView.adapter = null
        super.onDestroyView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(NOTE_INPUT_REQUEST_KEY) { _, bundle ->
            val note = bundle.getString(TextInputFragment.RESULT_KEY)
            viewModel.onNoteChanged(note.orEmpty())
        }
    }

}
