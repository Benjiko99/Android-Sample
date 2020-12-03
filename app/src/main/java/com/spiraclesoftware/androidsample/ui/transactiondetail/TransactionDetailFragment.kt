package com.spiraclesoftware.androidsample.ui.transactiondetail

import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.ColorUtils
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import co.zsmb.rainbowcake.base.OneShotEvent
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericFastAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.mikepenz.fastadapter.listeners.addClickListener
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.StandardFragment
import com.spiraclesoftware.androidsample.databinding.TransactionDetailFragmentBinding
import com.spiraclesoftware.androidsample.domain.model.TransactionCategory
import com.spiraclesoftware.androidsample.domain.model.TransactionId
import com.spiraclesoftware.androidsample.ui.imagepicker.ImagePicker
import com.spiraclesoftware.androidsample.ui.shared.DateTimeFormat
import com.spiraclesoftware.androidsample.ui.shared.DelightUI
import com.spiraclesoftware.androidsample.ui.textinput.TextInputFragment
import com.spiraclesoftware.androidsample.ui.transactiondetail.TransactionDetailViewModel.*
import com.spiraclesoftware.androidsample.ui.transactiondetail.cards.items.AttachmentsCardItem
import com.spiraclesoftware.core.extensions.*
import com.stfalcon.imageviewer.StfalconImageViewer
import io.cabriole.decorator.LinearMarginDecoration
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class TransactionDetailFragment :
    StandardFragment<TransactionDetailFragmentBinding, TransactionDetailViewState, TransactionDetailViewModel>() {

    override fun provideViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        TransactionDetailFragmentBinding.inflate(inflater, container, false)

    override fun provideViewModel(): TransactionDetailViewModel {
        TransactionDetailFragmentArgs.fromBundle(requireArguments()).let { args ->
            val id = TransactionId(args.transactionId)
            return getViewModel { parametersOf(id) }
        }
    }

    companion object {
        const val NOTE_INPUT_REQUEST_KEY = "noteInputRequest"
    }

    private lateinit var imagePicker: ImagePicker
    private lateinit var fastAdapter: GenericFastAdapter
    private lateinit var itemAdapter: GenericItemAdapter

    override fun render(viewState: TransactionDetailViewState): Unit = with(binding) {
        errorMessageView.isVisible = viewState is Error

        when (viewState) {
            is DetailReady -> {
                FastAdapterDiffUtil[itemAdapter] = viewState.cardItems

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
            is NavigateEvent -> {
                findNavController().navigate(event.navDirections)
            }
            is NavigateToCardDetailEvent -> {
                showToast(R.string.not_implemented, Toast.LENGTH_SHORT)
            }
            is DownloadStatementEvent -> {
                showToast(R.string.not_implemented, Toast.LENGTH_SHORT)
            }
            is OpenAttachmentPickerEvent -> {
                openAttachmentPicker(viewModel::onAttachmentPicked)
            }
            is OpenAttachmentViewerEvent -> {
                openAttachmentViewer(event.images, event.startPosition)
            }
            is NotifyAttachmentsLimitReachedEvent -> {
                showSnackbar(R.string.transaction_detail__attachments__error__limit_reached, Snackbar.LENGTH_LONG)
            }
            is NotifyOfFailureEvent -> {
                showToast(event.stringRes, Toast.LENGTH_LONG)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(NOTE_INPUT_REQUEST_KEY) { _, bundle ->
            val note = bundle.getString(TextInputFragment.RESULT_KEY)
            viewModel.onNoteChanged(note.orEmpty())
        }

        imagePicker = ImagePicker().also {
            it.registerResultListeners(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupFastItemAdapter()
        setupRecyclerView()
    }

    override fun onDestroyView() = with(binding) {
        recyclerView.adapter = null
        super.onDestroyView()
    }

    private fun openAttachmentPicker(onAttachmentPicked: (Uri) -> Unit) {
        imagePicker.showDialog(requireContext()) { imageUri ->
            onAttachmentPicked(imageUri)
        }
    }

    private fun openAttachmentViewer(images: List<Uri>, startPosition: Int) {
        StfalconImageViewer.Builder(context, images) { view, uri ->
            view.load(uri) {
                error(tintedDrawable(R.drawable.ic_image_error, Color.WHITE))
            }
        }
            .withStartPosition(startPosition)
            .withHiddenStatusBar(false)
            .show()
    }

    private fun setupToolbar() = with(binding) {
        toolbar.setupWithNavController(findNavController())
        DelightUI.setupToolbarTitleAppearingOnScroll(toolbar, scrollView) {
            toolbar.height + nameView.height
        }
    }

    private fun setupFastItemAdapter() {
        itemAdapter = ItemAdapter.items()
        fastAdapter = FastAdapter.with(itemAdapter).apply {
            // Prevent clicking on the CardView of each item
            attachDefaultListeners = false
            setHasStableIds(true)

            addClickListener({ vh: AttachmentsCardItem.ViewHolder -> vh.binding.actionView }) { _, _, _, _ ->
                viewModel.onAddAttachment()
            }
        }
    }

    private fun setupRecyclerView() = with(binding) {
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

    private fun bindAmountText(formattedMoney: String, contributesToBalance: Boolean) = with(binding) {
        amountView.text = formattedMoney
        if (!contributesToBalance) {
            amountView.addPaintFlag(Paint.STRIKE_THRU_TEXT_FLAG)
        }
    }

    private fun bindCategoryIcon(category: TransactionCategory, isSuccessful: Boolean) = with(binding) {
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

}