package com.spiraclesoftware.androidsample.ui.transactiondetail

import androidx.navigation.NavDirections
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.domain.model.TransactionId
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

    object OpenAttachmentPickerEvent : OneShotEvent

    object NavigateToCardDetailEvent : OneShotEvent

    object DownloadStatementEvent : OneShotEvent

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
                val contributesToBalance = detailPresenter.contributesToBalance(transaction)
                val isSuccessful = detailPresenter.isSuccessful(transaction)
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

    fun onNoteChanged(note: String) = executeNonBlocking {
        try {
            detailPresenter.updateNote(transactionId, note)
        } catch (e: Exception) {
            Timber.e(e)
            postEvent(NotifyOfFailureEvent(R.string.unknown_error))
        }
    }

    private fun openNoteInput() = execute {
        val navDirections = toTextInput(
            TextInputType.NOTE,
            NOTE_INPUT_REQUEST_KEY,
            initialValue = detailPresenter.getNote(transactionId)
        )

        postEvent(NavigateEvent(navDirections))
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

    private fun openCardDetail() {
        postEvent(NavigateToCardDetailEvent)
    }

    private fun downloadStatement() {
        postEvent(DownloadStatementEvent)
    }

    override fun onCardAction() = openCardDetail()

    override fun onStatementAction() = downloadStatement()

    override fun onCategoryAction() = openCategorySelect()

    override fun onAttachmentAction() = openAttachmentPicker()

    override fun onNoteAction() = openNoteInput()
}
