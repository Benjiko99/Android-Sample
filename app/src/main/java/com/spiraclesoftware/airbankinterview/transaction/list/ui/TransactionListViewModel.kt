package com.spiraclesoftware.airbankinterview.transaction.list.ui

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.spiraclesoftware.airbankinterview.transaction.list.data.TransactionDirectionFilter
import com.spiraclesoftware.airbankinterview.transaction.list.data.TransactionListFilter
import com.spiraclesoftware.airbankinterview.transaction.list.data.TransactionListRepository
import com.spiraclesoftware.airbankinterview.transaction.list.domain.Transaction
import com.spiraclesoftware.core.data.Resource
import javax.inject.Inject

class TransactionListViewModel @Inject constructor(
    private val repository: TransactionListRepository
) : ViewModel() {

    val transactions = MediatorLiveData<Resource<List<Transaction>>>()
    private val transactionListFilter = MutableLiveData<TransactionListFilter>()
    private val retryTransactionsTrigger = MutableLiveData<Nothing>()

    init {
        transactionListFilter.value = TransactionListFilter()

        val filterChanged = Transformations.switchMap(transactionListFilter) { filter ->
            repository.loadTransactionList(filter)
        }
        val retryTriggered = Transformations.switchMap(retryTransactionsTrigger) {
            repository.loadTransactionList(transactionListFilter.value!!)
        }

        transactions.addSource(filterChanged, transactions::setValue)
        transactions.addSource(retryTriggered, transactions::setValue)
    }

    fun setTransactionDirectionFilter(transactionDirectionFilter: TransactionDirectionFilter) {
        transactionListFilter.value?.transactionDirectionFilter = transactionDirectionFilter
        transactionListFilter.value = transactionListFilter.value
    }

    fun retry() {
        retryTransactionsTrigger.value = retryTransactionsTrigger.value
    }
}