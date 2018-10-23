package com.spiraclesoftware.airbankinterview.transaction.list.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.spiraclesoftware.airbankinterview.transaction.list.data.TransactionListRepository
import com.spiraclesoftware.airbankinterview.transaction.list.domain.Transaction
import com.spiraclesoftware.core.data.Resource
import javax.inject.Inject

class TransactionListViewModel @Inject constructor(
    val repository: TransactionListRepository
) : ViewModel() {

    private val _loadTransactionListEvent: MutableLiveData<Nothing> = MutableLiveData()

    val transactions: LiveData<Resource<List<Transaction>>> = Transformations
        .switchMap(_loadTransactionListEvent) { _ ->
            repository.loadTransactionList()
        }

    init {
        loadTransactionList()
    }

    private fun loadTransactionList() {
        _loadTransactionListEvent.value = _loadTransactionListEvent.value
    }

    fun retry() {
        loadTransactionList()
    }
}