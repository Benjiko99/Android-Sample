package com.spiraclesoftware.androidsample.feature.transaction_detail

import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.ColorUtils
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import co.zsmb.rainbowcake.base.OneShotEvent
import coil.load
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericFastAdapter
import com.mikepenz.fastadapter.adapters.GenericModelAdapter
import com.mikepenz.fastadapter.adapters.ModelAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.rubensousa.decorator.LinearMarginDecoration
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.component.image_picker.ImagePicker
import com.spiraclesoftware.androidsample.databinding.PrimaryActionChipBinding
import com.spiraclesoftware.androidsample.databinding.TransactionDetailFragmentBinding
import com.spiraclesoftware.androidsample.domain.entity.TransactionId
import com.spiraclesoftware.androidsample.feature.text_input.TextInputFragment
import com.spiraclesoftware.androidsample.feature.transaction_detail.TransactionDetailFragmentDirections.Companion.toCategorySelect
import com.spiraclesoftware.androidsample.feature.transaction_detail.TransactionDetailFragmentDirections.Companion.toTextInput
import com.spiraclesoftware.androidsample.feature.transaction_detail.TransactionDetailViewModel.*
import com.spiraclesoftware.androidsample.feature.transaction_detail.TransactionDetailViewState.*
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.item.*
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.item.model.*
import com.spiraclesoftware.androidsample.framework.core.Model
import com.spiraclesoftware.androidsample.framework.core.StandardFragment
import com.spiraclesoftware.androidsample.framework.extensions.*
import com.spiraclesoftware.androidsample.framework.utils.DelightUI
import com.stfalcon.imageviewer.StfalconImageViewer
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class TransactionDetailFragment :
    StandardFragment<TransactionDetailFragmentBinding, TransactionDetailViewState, TransactionDetailViewModel>(),
    AttachmentsCardItem.ActionListener {

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
    private lateinit var itemAdapter: GenericModelAdapter<Model>

    private fun onSelectCategory() {
        viewModel.selectCategory()
    }

    override fun onAddAttachment() {
        viewModel.addAttachment()
    }

    override fun onRemoveAttachment(uri: Uri) {
        viewModel.removeAttachment(uri)
    }

    override fun onViewAttachment(uri: Uri) {
        viewModel.viewAttachment(uri)
    }

    override fun onCancelUpload(uri: Uri) {
        viewModel.cancelUpload(uri)
    }

    private fun onChangeNote() {
        viewModel.openNoteInput()
    }

    private fun onChipAction(actionId: Int) {
        when (actionId) {
            R.id.action_split_bill -> viewModel.splitBill()
        }
    }

    private fun onValuePairAction(actionId: Int) {
        when (actionId) {
            R.id.action_open_card_detail -> viewModel.openCardDetail()
            R.id.action_download_statement -> viewModel.downloadStatement()
        }
    }

    private fun onNoteChanged(note: String?) {
        viewModel.onNoteChanged(note.orEmpty())
    }

    override fun render(viewState: TransactionDetailViewState): Unit = with(binding) {
        scrollView.isVisible = viewState is Content
        errorMessageView.isVisible = viewState is Error

        when (viewState) {
            is Initial -> {}
            is Content -> {
                FastAdapterDiffUtil[itemAdapter] = viewState.cardModels
                renderActionChips(viewState.actionChips)

                with(viewState.detailModel) {
                    toolbar.title = name
                    nameView.text = name
                    dateView.text = processingDate
                    renderAmountText(formattedMoney, contributesToBalance)
                    renderCategoryIcon(iconRes, iconTintRes)
                }
            }
            is Error -> {
                errorMessageView.text = viewState.message
            }
        }
    }

    private fun renderActionChips(actionChips: List<ActionChip>) = with(binding) {
        actionChips.forEach { chip ->
            actionChipsGroup.findViewById<Chip>(chip.id)?.let { view ->
                view.isVisible = chip.isVisible
            }
        }
        actionChipsGroup.isVisible = actionChipsGroup.children.any { it.isVisible }
    }

    private fun renderAmountText(formattedMoney: String, contributesToBalance: Boolean) = with(binding) {
        amountView.text = formattedMoney
        if (!contributesToBalance) {
            amountView.addPaintFlag(Paint.STRIKE_THRU_TEXT_FLAG)
        }
    }

    private fun renderCategoryIcon(iconRes: Int, iconTintRes: Int) = with(binding) {
        val tint: Int = color(iconTintRes)
        iconView.setImageDrawable(tintedDrawable(iconRes, tint))

        val fadedTint = ColorUtils.setAlphaComponent(tint, 255 / 100 * 15)
        iconView.background = tintedDrawable(R.drawable.shp_circle, fadedTint)
    }

    override fun onEvent(event: OneShotEvent) {
        when (event) {
            is NavigateToCategorySelectEvent -> {
                val directions = toCategorySelect(event.transactionId, event.initialCategory)
                findNavController().navigate(directions)
            }
            is NavigateToTextInputEvent -> {
                val directions = toTextInput(event.inputType, event.requestKey, event.initialInput)
                findNavController().navigate(directions)
            }
            is NavigateToCardDetailEvent -> {
                showToast(R.string.not_implemented, Toast.LENGTH_SHORT)
            }
            is SplitBillEvent -> {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(NOTE_INPUT_REQUEST_KEY) { _, bundle ->
            val note = bundle.getString(TextInputFragment.RESULT_KEY)
            onNoteChanged(note)
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
        setupActionChips()
    }

    override fun onDestroyView() = with(binding) {
        recyclerView.adapter = null
        super.onDestroyView()
    }

    private fun setupActionChips() = with(binding) {
        val chip = PrimaryActionChipBinding.inflate(layoutInflater, actionChipsGroup, false).root.apply {
            id = R.id.action_split_bill
            text = string(R.string.transaction_detail__split_bill)
            chipIcon = drawable(R.drawable.ic_split_bill)
        }
        actionChipsGroup.addView(chip)

        chip.setOnClickListener { onChipAction(it.id) }
    }

    private fun setupToolbar() = with(binding) {
        toolbar.setupWithNavController(findNavController())
        DelightUI.setupToolbarTitleAppearingOnScroll(toolbar, scrollView) {
            toolbar.height + nameView.height
        }
    }

    private fun setupFastItemAdapter() {
        itemAdapter = ModelAdapter.models { model: Model ->
            when (model) {
                is ValuePairCardModel ->
                    ValuePairCardItem(model, ::onValuePairAction)
                is StatusCardModel ->
                    StatusCardItem(model)
                is CategoryCardModel ->
                    CategoryCardItem(model, ::onSelectCategory)
                is AttachmentsCardModel ->
                    AttachmentsCardItem(model, this)
                is NoteCardModel ->
                    NoteCardItem(model, ::onChangeNote)
                else -> throw IllegalStateException()
            }
        }
        fastAdapter = FastAdapter.with(itemAdapter).apply {
            // Prevent clicking on the CardView of each item
            attachDefaultListeners = false
            setHasStableIds(true)
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

}