package com.spiraclesoftware.androidsample.features.transaction.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.spiraclesoftware.androidsample.shared.data.TransactionsRepository
import com.spiraclesoftware.androidsample.shared.domain.Transaction
import com.spiraclesoftware.androidsample.shared.domain.TransactionDirectionFilter
import com.spiraclesoftware.androidsample.shared.domain.TransactionId
import com.spiraclesoftware.androidsample.shared.domain.TransactionListFilter
import com.spiraclesoftware.core.data.*

class TransactionListViewModel(
    private val repository: TransactionsRepository
) : ViewModel() {

    val transactions: LiveData<Resource<List<Transaction>>>
    val transactionListFilter: LiveData<TransactionListFilter>
        get() = _transactionListFilter

    private val _transactionListFilter = MutableLiveData<TransactionListFilter>()
    private val retryTrigger = LiveTrigger()

    private val _navigateToDetailAction = MutableLiveData<Event<TransactionId>>()
    val navigateToDetailAction: LiveData<Event<TransactionId>> = _navigateToDetailAction

    init {
        // Define all events that should cause data to be reloaded.
        val triggers = MediatorLiveTrigger().apply {
            // trigger() just calls setValue() on the Mediator to cause the observers to be notified.
            addSource(_transactionListFilter) { trigger() }
            addSource(retryTrigger) { trigger() }
        }

        transactions = Transformations.switchMap(triggers) {
            if (_transactionListFilter.value != null) {
                repository.loadTransactionList(_transactionListFilter.value!!)
            } else {
                AbsentLiveData.create()
            }
        }
    }

    fun openTransactionDetail(transactionId: TransactionId) {
        _navigateToDetailAction.value = Event(transactionId)
    }

    fun retry() {
        retryTrigger.trigger()
    }

    fun setTransactionDirectionFilter(transactionDirectionFilter: TransactionDirectionFilter) {
        if (_transactionListFilter.value?.transactionDirectionFilter != transactionDirectionFilter) {
            _transactionListFilter.value = TransactionListFilter(transactionDirectionFilter)
        }
    }
}