package com.spiraclesoftware.androidsample.ui.transactiondetail

import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.shared.domain.TransactionId
import com.spiraclesoftware.androidsample.ui.transactiondetail.cards.CardsGenerator

class TransactionDetailViewModel(
    private val detailPresenter: TransactionDetailPresenter,
    private val cardsGenerator: CardsGenerator
) : RainbowCakeViewModel<TransactionDetailViewState>(Loading) {

    object FeatureNotImplementedEvent : OneShotEvent
    object LoadFailedEvent : OneShotEvent

    fun loadTransaction(transactionId: TransactionId) = execute {
        val transaction = try {
            detailPresenter.getTransactionById(transactionId)!!
        } catch (e: Exception) {
            postEvent(LoadFailedEvent)
            return@execute
        }

        val cards = cardsGenerator.makeCardsFor(transaction)

        viewState = DetailReady(transaction, cards)
    }

    fun onCardActionClicked(actionId: Int) {
        when (actionId) {
            R.id.card_action__card_detail,
            R.id.card_action__download_statement,
            R.id.card_action__change_category,
            R.id.card_action__change_note -> {
                postEvent(FeatureNotImplementedEvent)
            }
        }
    }

}
