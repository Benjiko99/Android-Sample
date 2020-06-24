package com.spiraclesoftware.androidsample.features.transaction.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.spiraclesoftware.androidsample.shared.data.AccountRepository
import com.spiraclesoftware.androidsample.shared.data.ConversionRatesRepository
import com.spiraclesoftware.androidsample.shared.data.TransactionsRepository
import com.spiraclesoftware.androidsample.shared.domain.*
import com.spiraclesoftware.core.data.*
import com.spiraclesoftware.core.extensions.zip

class TransactionListViewModel(
    accountRepo: AccountRepository,
    private val transactionsRepo: TransactionsRepository,
    private val ratesRepo: ConversionRatesRepository
) : ViewModel() {

    private val _account: LiveData<Account> = accountRepo.getAccount()

    private val _transactions: LiveData<Resource<List<Transaction>>>
    val transactionListFilter: LiveData<TransactionListFilter>
        get() = _transactionListFilter

    private val _transactionListFilter = MutableLiveData<TransactionListFilter>()
    private val refreshTrigger = LiveTrigger()

    private val _conversionRates: LiveData<Resource<ConversionRates>>

    private val _navigateToDetailAction = MutableLiveData<Event<TransactionId>>()

    val navigateToDetailAction: LiveData<Event<TransactionId>> = _navigateToDetailAction

    init {
        // Define all events that should cause data to be reloaded.
        val triggers = MediatorLiveTrigger().apply {
            // trigger() just calls setValue() on the Mediator to cause the observers to be notified.
            addSource(_transactionListFilter) { trigger() }
            addSource(refreshTrigger) { trigger() }
        }

        _transactions = Transformations.switchMap(triggers) {
            if (_transactionListFilter.value != null) {
                transactionsRepo.loadTransactionList(_transactionListFilter.value!!)
            } else {
                AbsentLiveData.create()
            }
        }

        _conversionRates = Transformations.switchMap(triggers) {
            ratesRepo.getConversionRates(_account.value!!.currency.currencyCode())
        }
    }

    val listData = zip(_account, _transactions, _conversionRates)

    fun openTransactionDetail(transactionId: TransactionId) {
        _navigateToDetailAction.value = Event(transactionId)
    }

    fun refresh() {
        transactionsRepo.dirtyCache()
        ratesRepo.dirtyCache()

        refreshTrigger.trigger()
    }

    fun setTransferDirectionFilter(transferDirectionFilter: TransferDirectionFilter) {
        if (_transactionListFilter.value?.transferDirectionFilter != transferDirectionFilter) {
            _transactionListFilter.value = TransactionListFilter(transferDirectionFilter)
        }
    }
}