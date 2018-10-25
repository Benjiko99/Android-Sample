package com.spiraclesoftware.airbankinterview.transaction.list.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.spiraclesoftware.airbankinterview.transaction.list.data.TransactionDirectionFilter
import com.spiraclesoftware.airbankinterview.transaction.list.data.TransactionListFilter
import com.spiraclesoftware.airbankinterview.transaction.list.data.TransactionListRepository
import com.spiraclesoftware.airbankinterview.transaction.list.domain.Transaction
import com.spiraclesoftware.core.data.LiveTrigger
import com.spiraclesoftware.core.data.MediatorLiveTrigger
import com.spiraclesoftware.core.data.Resource
import javax.inject.Inject

class TransactionListViewModel @Inject constructor(
    private val repository: TransactionListRepository
) : ViewModel() {

    val transactions: LiveData<Resource<List<Transaction>>>

    private val transactionListFilter = MutableLiveData<TransactionListFilter>()
    private val retryTrigger = LiveTrigger()

    init {
        transactionListFilter.value = TransactionListFilter()

        // Define all events that should cause data to be reloaded.
        val triggers = MediatorLiveTrigger().apply {
            // trigger() just calls setValue() on the Mediator to cause the observers to be notified.
            addSource(transactionListFilter) { trigger() }
            addSource(retryTrigger) { trigger() }
        }

        transactions = Transformations.switchMap(triggers) {
            repository.loadTransactionList(transactionListFilter.value!!)
        }
    }

    fun retry() {
        retryTrigger.trigger()
    }

    fun setTransactionDirectionFilter(transactionDirectionFilter: TransactionDirectionFilter) {
        transactionListFilter.value?.transactionDirectionFilter = transactionDirectionFilter
        // trigger the observers to be notified
        transactionListFilter.value = transactionListFilter.value
    }
}