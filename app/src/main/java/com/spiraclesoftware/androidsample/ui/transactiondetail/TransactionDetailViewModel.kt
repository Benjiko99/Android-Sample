package com.spiraclesoftware.androidsample.ui.transactiondetail

import androidx.navigation.NavDirections
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.domain.model.TransactionId
import com.spiraclesoftware.androidsample.ui.shared.MoneyFormat
import com.spiraclesoftware.androidsample.ui.textinput.TextInputType
import com.spiraclesoftware.androidsample.ui.transactiondetail.TransactionDetailFragment.Companion.NOTE_INPUT_REQUEST_KEY
import com.spiraclesoftware.androidsample.ui.transactiondetail.TransactionDetailFragmentDirections.Companion.toTextInput
import com.spiraclesoftware.androidsample.ui.transactiondetail.cards.CardsPresenter
import timber.log.Timber

class TransactionDetailViewModel(
    private val transactionId: TransactionId,
    private val detailPresenter: TransactionDetailPresenter,
    private val cardsPresenter: CardsPresenter
) : RainbowCakeViewModel<TransactionDetailViewState>(Loading) {

    data class NavigateToNoteInputEvent(val navDirections: NavDirections) : OneShotEvent

    data class NotifyOfFailureEvent(val stringRes: Int) : OneShotEvent

    object FeatureNotImplementedEvent : OneShotEvent

    fun loadData() = execute {
        viewState = Loading
        try {
            val transaction = detailPresenter.getTransactionById(transactionId)!!
            val cardItems = cardsPresenter.getCardItems(transaction, ::onCardActionClicked)
            val contributesToBalance = detailPresenter.contributesToBalance(transaction)
            val isSuccessful = detailPresenter.isSuccessful(transaction)
            val formattedMoney = MoneyFormat(transaction.signedMoney).format(transaction)

            Timber.d("Loaded data for detail; transactionId=$transactionId")
            viewState = DetailReady(
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
            viewState = Error
            return@execute
        }
    }

    fun retry() {
        loadData()
    }

    fun onNoteChanged(note: String) = executeNonBlocking {
        try {
            updateNote(note)

            // get updated card items, with new note in them
            val transaction = detailPresenter.getTransactionById(transactionId)!!
            val cardItems = cardsPresenter.getCardItems(transaction, ::onCardActionClicked)

            viewState = (viewState as? DetailReady)?.copy(
                cardItems = cardItems
            ) ?: viewState
        } catch (e: Exception) {
            Timber.e(e, "Failure while trying to change note")
            postEvent(NotifyOfFailureEvent(R.string.unknown_error))
        }
    }

    private suspend fun updateNote(note: String) {
        detailPresenter.updateNote(transactionId, note)
        Timber.d("Note was changed to \"$note\"")
    }

    private suspend fun openNoteInput() {
        val navDirections = toTextInput(
            TextInputType.NOTE,
            NOTE_INPUT_REQUEST_KEY,
            initialValue = detailPresenter.getNote(transactionId)
        )

        postEvent(NavigateToNoteInputEvent(navDirections))
        Timber.d("User navigated to Note Input")
    }

    fun onCardActionClicked(actionId: Int) {
        when (actionId) {
            R.id.card_action__change_note -> execute { openNoteInput() }
            R.id.card_action__card_detail,
            R.id.card_action__download_statement,
            R.id.card_action__change_category -> {
                postEvent(FeatureNotImplementedEvent)
                Timber.d("User clicked on an unimplemented card action")
            }
        }
    }

}
