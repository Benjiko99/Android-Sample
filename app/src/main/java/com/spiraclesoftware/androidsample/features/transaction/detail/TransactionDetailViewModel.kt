package com.spiraclesoftware.androidsample.features.transaction.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.features.transaction.detail.cards.Card
import com.spiraclesoftware.androidsample.features.transaction.detail.cards.CardBuilder
import com.spiraclesoftware.androidsample.features.transaction.detail.cards.CardValuePairs
import com.spiraclesoftware.androidsample.shared.data.TransactionsRepository
import com.spiraclesoftware.androidsample.shared.domain.Transaction
import com.spiraclesoftware.androidsample.shared.domain.TransactionId
import com.spiraclesoftware.androidsample.shared.domain.TransactionStatus
import com.spiraclesoftware.androidsample.shared.domain.TransactionStatusCode
import com.spiraclesoftware.core.data.Event
import com.spiraclesoftware.core.data.Resource
import com.spiraclesoftware.core.extensions.map
import com.spiraclesoftware.core.extensions.switchMap

class TransactionDetailViewModel(
    private val repository: TransactionsRepository
) : ViewModel() {

    val transaction: LiveData<Resource<Transaction>>
    val cards: LiveData<List<Card>>

    private val transactionId = MutableLiveData<TransactionId>()

    private val _notifyFeatureNotImplementedAction = MutableLiveData<Event<Nothing>>()
    val notifyFeatureNotImplementedAction: LiveData<Event<Nothing>> = _notifyFeatureNotImplementedAction

    init {
        transaction = transactionId.switchMap {
            repository.loadTransaction(transactionId.value!!)
        }

        cards = transaction.map {
            if (it.data != null) getCards(it.data!!) else emptyList()
        }
    }

    private fun getCards(transaction: Transaction): List<Card> {
        val statusCard = CardBuilder().apply {
            if (transaction.statusCode != TransactionStatusCode.SUCCESSFUL) {
                with(CardValuePairs.TransactionStatus(expanded = true))
            }
        }.build()

        val infoCard = CardBuilder().apply {
            if (transaction.statusCode == TransactionStatusCode.SUCCESSFUL) {
                with(CardValuePairs.TransactionStatus(expanded = false))
            }

            if (!transaction.cardDescription.isNullOrEmpty()) {
                with(CardValuePairs.CardDescription())
            }

            if (transaction.status == TransactionStatus.COMPLETED) {
                with(CardValuePairs.TransactionStatement())
            }
        }.build()

        val categoryCard = CardBuilder().apply {
            if (transaction.statusCode == TransactionStatusCode.SUCCESSFUL) {
                with(CardValuePairs.TransactionCategory())
            }
        }.build()

        val noteCard = CardBuilder().apply {
            with(CardValuePairs.NoteToSelf())
        }.build()

        val cards = arrayListOf(statusCard, infoCard, categoryCard, noteCard)
        return cards.filterNotNull()
    }

    fun onCardActionClicked(actionId: Int) {
        when (actionId) {
            R.id.card_action__card_detail,
            R.id.card_action__download_statement,
            R.id.card_action__change_category,
            R.id.card_action__change_note -> {
                _notifyFeatureNotImplementedAction.value = Event()
            }
        }
    }

    fun setTransactionId(id: TransactionId) {
        if (transactionId.value != id) {
            transactionId.value = id
        }
    }
}