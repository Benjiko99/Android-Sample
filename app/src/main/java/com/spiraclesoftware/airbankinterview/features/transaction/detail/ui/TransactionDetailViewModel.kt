package com.spiraclesoftware.airbankinterview.features.transaction.detail.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.spiraclesoftware.airbankinterview.shared.data.TransactionsRepository
import com.spiraclesoftware.airbankinterview.shared.domain.Transaction
import com.spiraclesoftware.airbankinterview.shared.domain.TransactionDetail
import com.spiraclesoftware.airbankinterview.shared.domain.TransactionId
import com.spiraclesoftware.core.data.LiveTrigger
import com.spiraclesoftware.core.data.MediatorLiveTrigger
import com.spiraclesoftware.core.data.Resource
import javax.inject.Inject

class TransactionDetailViewModel @Inject constructor(
    private val repository: TransactionsRepository
) : ViewModel() {

    val transaction: LiveData<Resource<Transaction>>
    val transactionDetail: LiveData<Resource<TransactionDetail>>

    private val transactionId = MutableLiveData<TransactionId>()
    private val retryTrigger = LiveTrigger()

    init {
        // Define all events that should cause data to be reloaded.
        val triggers = MediatorLiveTrigger().apply {
            // trigger() just calls setValue() on the Mediator to cause the observers to be notified.
            addSource(transactionId) { trigger() }
            addSource(retryTrigger) { trigger() }
        }

        transaction = Transformations.switchMap(triggers) {
            repository.loadTransaction(transactionId.value!!)
        }

        transactionDetail = Transformations.switchMap(triggers) {
            repository.loadTransactionDetail(transactionId.value!!)
        }
    }

    fun retry() {
        retryTrigger.trigger()
    }

    fun setTransactionId(id: TransactionId) {
        if (transactionId.value != id) {
            transactionId.value = id
        }
    }
}