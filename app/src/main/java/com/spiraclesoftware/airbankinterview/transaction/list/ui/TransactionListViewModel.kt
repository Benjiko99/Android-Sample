package com.spiraclesoftware.airbankinterview.transaction.list.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.spiraclesoftware.airbankinterview.transaction.list.data.TransactionListRepository
import com.spiraclesoftware.airbankinterview.transaction.list.domain.Transaction
import com.spiraclesoftware.core.data.Resource
import javax.inject.Inject

class TransactionListViewModel @Inject constructor(
    val repository: TransactionListRepository
) : ViewModel() {

    private lateinit var transactions: LiveData<Resource<List<Transaction>>>

    fun getTransactions(): LiveData<Resource<List<Transaction>>> {
        if (!::transactions.isInitialized) {
            transactions = MutableLiveData()
            loadTransactions()
        }
        return transactions
    }

    private fun loadTransactions() {
        transactions = repository.loadTransactionList()
    }
}