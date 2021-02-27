package com.spiraclesoftware.androidsample.feature.transaction_detail

import android.net.Uri
import androidx.core.net.toUri
import androidx.navigation.NavDirections
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.mikepenz.fastadapter.GenericItem
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.domain.entity.Transaction
import com.spiraclesoftware.androidsample.domain.entity.TransactionId
import com.spiraclesoftware.androidsample.feature.text_input.TextInputType
import com.spiraclesoftware.androidsample.feature.transaction_detail.TransactionDetailFragment.Companion.NOTE_INPUT_REQUEST_KEY
import com.spiraclesoftware.androidsample.feature.transaction_detail.TransactionDetailFragmentDirections.Companion.toCategorySelect
import com.spiraclesoftware.androidsample.feature.transaction_detail.TransactionDetailFragmentDirections.Companion.toTextInput
import com.spiraclesoftware.androidsample.feature.transaction_detail.TransactionDetailViewState.*
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import timber.log.Timber

class TransactionDetailViewModel(
    private val transactionId: TransactionId,
    private val detailPresenter: TransactionDetailPresenter,
    private val cardsPresenter: CardsPresenter
) : RainbowCakeViewModel<TransactionDetailViewState>(Initial), CardActionsHandler {

    data class NavigateEvent(val navDirections: NavDirections) : OneShotEvent

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

    data class ViewData(
        val detailModel: DetailModel,
        val uploads: List<Uri>
    )

    init {
        produceViewStateFromDataFlow()
    }

    private fun produceViewStateFromDataFlow() = executeNonBlocking {
        detailPresenter.flowDetailModel(transactionId)
            .combine(attachmentUploads) { detailModel, uploads -> ViewData(detailModel, uploads) }
            .collect { (detailModel, uploads) ->
                viewState = try {
                    val cardItems = getCardItems(detailModel.transaction, uploads)
                    Content(detailModel, cardItems)
                } catch (e: Exception) {
                    Timber.e(e)
                    Error
                }
            }
    }

    private fun getCardItems(
        transaction: Transaction,
        uploads: List<Uri>
    ): List<GenericItem> {
        val handler = this
        return cardsPresenter.getCards(transaction).map { card ->
            when (card) {
                is ValuePairCard ->
                    card.toListItem(transaction, handler)
                is StatusCard ->
                    card.toListItem(transaction.status, transaction.statusCode)
                is CategoryCard ->
                    card.toListItem(transaction.category, handler)
                is AttachmentsCard ->
                    card.toListItem(transaction.attachments, uploads, handler)
                is NoteCard ->
                    card.toListItem(transaction.noteToSelf, handler)
            }
        }
    }

    override fun onOpenCardDetail() {
        postEvent(NavigateToCardDetailEvent)
    }

    override fun onDownloadStatement() {
        postEvent(DownloadStatementEvent)
    }

    override fun onSelectCategory() = execute {
        val navDirections = toCategorySelect(
            transactionId.value,
            initialCategory = detailPresenter.getCategory(transactionId)!!
        )
        postEvent(NavigateEvent(navDirections))
    }

    override fun onViewAttachment(uri: Uri) = execute {
        val transaction = detailPresenter.getTransactionById(transactionId)!!
        val images = transaction.attachments.map(String::toUri)
        val startPosition = images.indexOf(uri)
        postEvent(OpenAttachmentViewerEvent(images, startPosition))
    }

    override fun onAddAttachment() = execute {
        val transaction = detailPresenter.getTransactionById(transactionId)!!
        val totalCount = transaction.attachments.size + attachmentUploads.value.size

        if (totalCount >= Transaction.MAX_ATTACHMENTS) {
            postEvent(NotifyAttachmentsLimitReachedEvent)
            return@execute
        }
        postEvent(OpenAttachmentPickerEvent)
    }

    override fun onRemoveAttachment(uri: Uri) = execute {
        detailPresenter.removeAttachment(transactionId, uri)
    }

    override fun onCancelUpload(uri: Uri) {
        // Not yet implemented
    }

    override fun onChangeNote() = execute {
        val navDirections = toTextInput(
            TextInputType.NOTE,
            NOTE_INPUT_REQUEST_KEY,
            initialValue = detailPresenter.getNote(transactionId)
        )

        postEvent(NavigateEvent(navDirections))
    }

    fun onAttachmentPicked(imageUri: Uri) = executeNonBlocking {
        attachmentUploads.value = attachmentUploads.value.plus(imageUri)

        try {
            delay(3000)
            detailPresenter.uploadAttachment(transactionId, imageUri)
            attachmentUploads.value = attachmentUploads.value.minus(imageUri)
        } catch (e: Exception) {
            Timber.e(e)
            postEvent(NotifyOfFailureEvent(R.string.unknown_error))
        }
    }

    fun onNoteChanged(note: String) = executeNonBlocking {
        try {
            detailPresenter.updateNote(transactionId, note)
        } catch (e: Exception) {
            Timber.e(e)
            postEvent(NotifyOfFailureEvent(R.string.unknown_error))
        }
    }

}
