package com.spiraclesoftware.androidsample.ui.transactiondetail

import android.net.Uri
import androidx.navigation.NavDirections
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.mikepenz.fastadapter.GenericItem
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.domain.model.TransactionId
import com.spiraclesoftware.androidsample.ui.shared.MoneyFormat
import com.spiraclesoftware.androidsample.ui.textinput.TextInputType
import com.spiraclesoftware.androidsample.ui.transactiondetail.TransactionDetailFragment.Companion.NOTE_INPUT_REQUEST_KEY
import com.spiraclesoftware.androidsample.ui.transactiondetail.TransactionDetailFragmentDirections.Companion.toCategorySelect
import com.spiraclesoftware.androidsample.ui.transactiondetail.TransactionDetailFragmentDirections.Companion.toTextInput
import com.spiraclesoftware.androidsample.ui.transactiondetail.cards.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import timber.log.Timber

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionDetailViewModel(
    private val transactionId: TransactionId,
    private val detailPresenter: TransactionDetailPresenter,
    private val cardsPresenter: CardsPresenter
) : RainbowCakeViewModel<TransactionDetailViewState>(Loading), CardActionsHandler {

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

    data class ScreenData(
        val transaction: Transaction,
        val uploads: List<Uri>
    )

    init {
        executeNonBlocking {
            collectScreenData()
        }
    }

    suspend fun collectScreenData() =
        detailPresenter.flowTransactionById(transactionId)
            .combine(attachmentUploads) { transaction, uploads -> ScreenData(transaction!!, uploads) }
            .collect { (transaction, uploads) ->
                viewState = try {
                    val cardItems = getCardItems(transaction, uploads)
                    val contributesToBalance = transaction.contributesToAccountBalance()
                    val isSuccessful = transaction.isSuccessful()
                    val formattedMoney = MoneyFormat(transaction.signedMoney).format(transaction)

                    Content(
                        transaction.name,
                        transaction.processingDate,
                        formattedMoney,
                        contributesToBalance,
                        isSuccessful,
                        transaction.category,
                        cardItems
                    )
                } catch (e: Exception) {
                    Timber.e(e)
                    Error
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
        val startPosition = transaction.attachments.indexOf(uri)
        postEvent(OpenAttachmentViewerEvent(transaction.attachments, startPosition))
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
