package com.spiraclesoftware.androidsample.ui.transactiondetail

import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.domain.model.TransactionId
import com.spiraclesoftware.androidsample.domain.model.TransactionStatusCode

class TransactionDetailViewModel(
    private val detailPresenter: TransactionDetailPresenter
) : RainbowCakeViewModel<TransactionDetailViewState>(Loading) {

    object FeatureNotImplementedEvent : OneShotEvent
    object LoadFailedEvent : OneShotEvent

    fun loadData(transactionId: TransactionId) = execute {
        try {
            val transaction = detailPresenter.getTransactionById(transactionId)!!
            val cardItems = detailPresenter.getCardItems(transaction, ::onCardActionClicked)
            viewState = DetailReady(
                transaction.name,
                transaction.processingDate,
                transaction.formattedMoney,
                transaction.contributesToBalance(),
                transaction.statusCode == TransactionStatusCode.SUCCESSFUL,
                transaction.category,
                cardItems
            )
        } catch (e: Exception) {
            postEvent(LoadFailedEvent)
            return@execute
        }
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
