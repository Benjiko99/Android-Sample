package com.spiraclesoftware.androidsample.ui.transactionlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.spiraclesoftware.androidsample.shared.domain.TransactionId
import com.spiraclesoftware.androidsample.shared.domain.TransactionListFilter
import com.spiraclesoftware.androidsample.shared.domain.TransferDirectionFilter

class TransactionListViewModel(
    private val listPresenter: TransactionListPresenter
) : RainbowCakeViewModel<TransactionListViewState>(Loading) {

    data class NavigateToDetailEvent(val id: TransactionId) : OneShotEvent

    val listFilter: LiveData<TransactionListFilter> get() = _listFilter
    private val _listFilter = MutableLiveData(TransactionListFilter(TransferDirectionFilter.ALL))

    init {
        execute { loadData() }
    }

    fun reload() {
        execute { loadData(ignoreCached = true) }
    }

    /**
     * @param ignoreCached whether to request latest from the network instead of using cached data
     */
    private suspend fun loadData(ignoreCached: Boolean = false) {
        viewState = Loading
        viewState = try {
            val account = listPresenter.getAccount()
            val conversionRates = listPresenter.getConversionRates(ignoreCached)
            val transactions = listPresenter.getTransactions(_listFilter.value!!, ignoreCached)
            ListReady(
                account!!,
                transactions!!,
                conversionRates!!
            )
        } catch (e: Exception) {
            NetworkError
        }
    }

    fun setTransferDirectionFilter(filter: TransferDirectionFilter) {
        if (_listFilter.value?.transferDirectionFilter != filter) {
            _listFilter.value = TransactionListFilter(filter)

            updateFilteredTransactions()
        }
    }

    /** Updates the [viewState] with transactions that are using the latest filter. **/
    private fun updateFilteredTransactions() {
        execute {
            listPresenter.getTransactions(_listFilter.value!!)?.let { transactions ->
                viewState = (viewState as? ListReady)?.copy(transactions = transactions) ?: viewState
            }
        }
    }

    fun onListItemClicked(id: TransactionId) {
        postEvent(NavigateToDetailEvent(id))
    }

}
