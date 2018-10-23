package com.spiraclesoftware.airbankinterview.transaction.detail.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.spiraclesoftware.airbankinterview.transaction.detail.data.TransactionDetailRepository
import com.spiraclesoftware.airbankinterview.transaction.detail.domain.TransactionDetail
import com.spiraclesoftware.airbankinterview.transaction.list.data.TransactionListRepository
import com.spiraclesoftware.airbankinterview.transaction.list.domain.Transaction
import com.spiraclesoftware.airbankinterview.transaction.list.domain.TransactionId
import com.spiraclesoftware.core.data.Resource
import javax.inject.Inject

class TransactionDetailViewModel @Inject constructor(
    val detailRepository: TransactionDetailRepository,
    val listRepository: TransactionListRepository
) : ViewModel() {

    private val _transactionId: MutableLiveData<TransactionId> = MutableLiveData()
    val transactionId: LiveData<TransactionId>
        get() = _transactionId

    val transaction: LiveData<Resource<Transaction>> = Transformations
        .switchMap(_transactionId) { input ->
            listRepository.loadTransaction(input)
        }

    val transactionDetail: LiveData<Resource<TransactionDetail>> = Transformations
        .switchMap(_transactionId) { input ->
            detailRepository.loadTransactionDetail(input)
        }

    fun retry() {
        _transactionId.value = _transactionId.value
    }

    fun setTransactionId(id: TransactionId) {
        if (_transactionId.value == id) {
            return
        }
        _transactionId.value = id
    }
}