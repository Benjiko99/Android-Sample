package com.spiraclesoftware.airbankinterview.features.transaction.list.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.spiraclesoftware.airbankinterview.shared.domain.TransactionDirectionFilter
import com.spiraclesoftware.airbankinterview.shared.domain.TransactionListFilter
import com.spiraclesoftware.airbankinterview.shared.data.TransactionsRepository
import com.spiraclesoftware.airbankinterview.shared.domain.Transaction
import com.spiraclesoftware.core.data.AbsentLiveData
import com.spiraclesoftware.core.data.LiveTrigger
import com.spiraclesoftware.core.data.MediatorLiveTrigger
import com.spiraclesoftware.core.data.Resource
import javax.inject.Inject

class TransactionListViewModel @Inject constructor(
    private val repository: TransactionsRepository
) : ViewModel() {

    val transactions: LiveData<Resource<List<Transaction>>>
    val transactionListFilter: LiveData<TransactionListFilter>
        get() = _transactionListFilter

    private val _transactionListFilter = MutableLiveData<TransactionListFilter>()
    private val retryTrigger = LiveTrigger()

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

    fun retry() {
        retryTrigger.trigger()
    }

    fun setTransactionDirectionFilter(transactionDirectionFilter: TransactionDirectionFilter) {
        if (_transactionListFilter.value?.transactionDirectionFilter == transactionDirectionFilter) return

        _transactionListFilter.value =
                TransactionListFilter(transactionDirectionFilter)
    }
}