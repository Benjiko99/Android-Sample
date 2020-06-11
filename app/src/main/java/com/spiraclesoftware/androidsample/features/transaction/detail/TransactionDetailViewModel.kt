package com.spiraclesoftware.androidsample.features.transaction.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.spiraclesoftware.androidsample.shared.data.TransactionsRepository
import com.spiraclesoftware.androidsample.shared.domain.Transaction
import com.spiraclesoftware.androidsample.shared.domain.TransactionId
import com.spiraclesoftware.core.data.MediatorLiveTrigger
import com.spiraclesoftware.core.data.Resource

class TransactionDetailViewModel(
    private val repository: TransactionsRepository
) : ViewModel() {

    val transaction: LiveData<Resource<Transaction>>

    private val transactionId = MutableLiveData<TransactionId>()

    init {
        // Define all events that should cause data to be reloaded.
        val triggers = MediatorLiveTrigger().apply {
            // trigger() just calls setValue() on the Mediator to cause the observers to be notified.
            addSource(transactionId) { trigger() }
        }

        transaction = Transformations.switchMap(triggers) {
            repository.loadTransaction(transactionId.value!!)
        }
    }

    fun setTransactionId(id: TransactionId) {
        if (transactionId.value != id) {
            transactionId.value = id
        }
    }
}