package com.spiraclesoftware.androidsample.feature.transaction_detail

import android.net.Uri
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.domain.entity.Transaction
import com.spiraclesoftware.androidsample.domain.entity.TransactionCategory
import com.spiraclesoftware.androidsample.domain.entity.TransactionId
import com.spiraclesoftware.androidsample.feature.text_input.TextInputType
import com.spiraclesoftware.androidsample.feature.transaction_detail.TransactionDetailFragment.Companion.NOTE_INPUT_REQUEST_KEY
import com.spiraclesoftware.androidsample.feature.transaction_detail.TransactionDetailViewState.*
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import timber.log.Timber

class TransactionDetailViewModel(
    private val transactionId: TransactionId,
    private val presenter: TransactionDetailPresenter
) : RainbowCakeViewModel<TransactionDetailViewState>(Initial) {

    data class NavigateToCategorySelectEvent(
        val transactionId: String,
        val initialCategory: TransactionCategory
    ) : OneShotEvent

    data class NavigateToTextInputEvent(
        val inputType: TextInputType,
        val requestKey: String,
        val initialInput: String
    ) : OneShotEvent

    object NavigateToCardDetailEvent : OneShotEvent

    object DownloadStatementEvent : OneShotEvent

    object OpenAttachmentPickerEvent : OneShotEvent

    data class OpenAttachmentViewerEvent(
        val images: List<Uri>,
        val startPosition: Int
    ) : OneShotEvent

    object NotifyAttachmentsLimitReachedEvent : OneShotEvent

    data class NotifyOfFailureEvent(val stringRes: Int) : OneShotEvent

    private val attachmentUploads = MutableStateFlow<List<Uri>>(emptyList())

    init {
        collectViewState()
    }

    private fun collectViewState() = executeNonBlocking {
        val detailModelFlow = presenter.flowDetailModel(transactionId)
        val cardModelsFlow = presenter.flowCardModels(transactionId, attachmentUploads)

        detailModelFlow.combine(cardModelsFlow) { detailModel, cardModels ->
            Content(detailModel, cardModels)
        }
            .catch { viewState = Error(it.message) }
            .collect { viewState = it }
    }

    fun openCardDetail() {
        postEvent(NavigateToCardDetailEvent)
    }

    fun downloadStatement() {
        postEvent(DownloadStatementEvent)
    }

    fun selectCategory() = execute {
        val navigateEvent = NavigateToCategorySelectEvent(
            transactionId.value,
            presenter.getCategory()
        )
        postEvent(navigateEvent)
    }

    fun viewAttachment(uri: Uri) = execute {
        val images = presenter.getAttachments()
        val startPosition = images.indexOf(uri)
        postEvent(OpenAttachmentViewerEvent(images, startPosition))
    }

    fun addAttachment() = execute {
        val attachments = presenter.getAttachments()
        val totalCount = attachments.size + attachmentUploads.value.size

        if (totalCount >= Transaction.MAX_ATTACHMENTS) {
            postEvent(NotifyAttachmentsLimitReachedEvent)
            return@execute
        }
        postEvent(OpenAttachmentPickerEvent)
    }

    fun removeAttachment(uri: Uri) = execute {
        presenter.removeAttachment(uri)
    }

    fun cancelUpload(uri: Uri) {
        // Not yet implemented
    }

    fun openNoteInput() = execute {
        val navigateEvent = NavigateToTextInputEvent(
            TextInputType.NOTE,
            NOTE_INPUT_REQUEST_KEY,
            initialInput = presenter.getNote()
        )
        postEvent(navigateEvent)
    }

    fun onAttachmentPicked(imageUri: Uri) = executeNonBlocking {
        attachmentUploads.value = attachmentUploads.value.plus(imageUri)

        try {
            delay(3000) // Simulate network delay
            presenter.uploadAttachment(imageUri)
            attachmentUploads.value = attachmentUploads.value.minus(imageUri)
        } catch (e: Exception) {
            Timber.e(e)
            postEvent(NotifyOfFailureEvent(R.string.unknown_error))
        }
    }

    fun onNoteChanged(note: String) = executeNonBlocking {
        try {
            presenter.updateNote(note)
        } catch (e: Exception) {
            Timber.e(e)
            postEvent(NotifyOfFailureEvent(R.string.unknown_error))
        }
    }

}
