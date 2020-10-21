package com.spiraclesoftware.androidsample.ui.transactiondetail

import androidx.navigation.NavDirections
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.domain.model.TransactionId
import com.spiraclesoftware.androidsample.domain.policy.TransactionsPolicy
import com.spiraclesoftware.androidsample.ui.shared.MoneyFormat
import com.spiraclesoftware.androidsample.ui.textinput.TextInputType
import com.spiraclesoftware.androidsample.ui.transactiondetail.TransactionDetailFragment.Companion.NOTE_INPUT_REQUEST_KEY
import com.spiraclesoftware.androidsample.ui.transactiondetail.TransactionDetailFragmentDirections.Companion.toCategorySelect
import com.spiraclesoftware.androidsample.ui.transactiondetail.TransactionDetailFragmentDirections.Companion.toTextInput
import com.spiraclesoftware.androidsample.ui.transactiondetail.cards.CardActionsHandler
import com.spiraclesoftware.androidsample.ui.transactiondetail.cards.CardsPresenter
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class TransactionDetailViewModel(
    private val transactionId: TransactionId,
    private val detailPresenter: TransactionDetailPresenter,
    private val cardsPresenter: CardsPresenter
) : RainbowCakeViewModel<TransactionDetailViewState>(Loading), CardActionsHandler {

    data class NavigateEvent(val navDirections: NavDirections) : OneShotEvent

    object NavigateToCardDetailEvent : OneShotEvent

    object DownloadStatementEvent : OneShotEvent

    object OpenAttachmentPickerEvent : OneShotEvent

    data class OpenAttachmentViewerEvent(val images: List<String>, val startPosition: Int) : OneShotEvent

    object RemoveAttachmentEvent : OneShotEvent

    object NotifyAttachmentsLimitReachedEvent : OneShotEvent
    
    data class NotifyOfFailureEvent(val stringRes: Int) : OneShotEvent

    init {
        executeNonBlocking {
            collectTransaction()
        }
    }

    suspend fun collectTransaction() =
        detailPresenter.flowTransactionById(transactionId).collect { transaction ->
            viewState = try {
                val cardItems = cardsPresenter.getCardItems(transaction!!, this)
                val contributesToBalance = TransactionsPolicy.contributesToBalance(transaction)
                val isSuccessful = TransactionsPolicy.isSuccessful(transaction)
                val formattedMoney = MoneyFormat(transaction.signedMoney).format(transaction)

                DetailReady(
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

    override fun onOpenCardDetail() = openCardDetail()

    override fun onDownloadStatement() = downloadStatement()

    override fun onSelectCategory() = openCategorySelect()

    override fun onAddAttachment() = execute {
        val transaction = detailPresenter.getTransactionById(transactionId)!!
        if (TransactionsPolicy.isAttachmentsLimitReached(transaction)) {
            postEvent(NotifyAttachmentsLimitReachedEvent)
            return@execute
        }
        openAttachmentPicker()
    }

    override fun onRemoveAttachment(url: String) = removeAttachment(url)

    override fun onViewAttachment(url: String) = openAttachmentViewer(url)

    override fun onChangeNote() = openNoteInput()

    fun onNoteChanged(note: String) = executeNonBlocking {
        try {
            detailPresenter.updateNote(transactionId, note)
        } catch (e: Exception) {
            Timber.e(e)
            postEvent(NotifyOfFailureEvent(R.string.unknown_error))
        }
    }

    private fun removeAttachment(url: String) = execute {
        detailPresenter.removeAttachment(transactionId, url)
        postEvent(RemoveAttachmentEvent)
    }

    private fun downloadStatement() {
        postEvent(DownloadStatementEvent)
    }

    private fun openCardDetail() {
        postEvent(NavigateToCardDetailEvent)
    }

    private fun openCategorySelect() = execute {
        val navDirections = toCategorySelect(
            transactionId.value,
            initialCategory = detailPresenter.getCategory(transactionId)!!
        )
        postEvent(NavigateEvent(navDirections))
    }

    private fun openAttachmentPicker() {
        postEvent(OpenAttachmentPickerEvent)
    }
    
    private fun openAttachmentViewer(url: String) = execute {
        val transaction = detailPresenter.getTransactionById(transactionId)!!
        val startPosition = transaction.attachments.indexOf(url)
        postEvent(OpenAttachmentViewerEvent(transaction.attachments, startPosition))
    }

    private fun openNoteInput() = execute {
        val navDirections = toTextInput(
            TextInputType.NOTE,
            NOTE_INPUT_REQUEST_KEY,
            initialValue = detailPresenter.getNote(transactionId)
        )

        postEvent(NavigateEvent(navDirections))
    }

}
