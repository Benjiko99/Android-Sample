package com.spiraclesoftware.androidsample.ui.transactionlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.spiraclesoftware.androidsample.domain.model.TransactionId
import com.spiraclesoftware.androidsample.domain.model.TransactionListFilter
import com.spiraclesoftware.androidsample.domain.model.TransferDirectionFilter

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
            val transactions = listPresenter.getListItems(_listFilter.value!!, ignoreCached)
            ListReady(transactions)
        } catch (e: Exception) {
            NetworkError
        }
    }

    fun setTransferDirectionFilter(filter: TransferDirectionFilter) {
        if (_listFilter.value?.transferDirectionFilter != filter) {
            _listFilter.value = TransactionListFilter(filter)

            updateListFilter()
        }
    }

    /** Updates the [viewState] with list items that are using the latest filter. **/
    private fun updateListFilter() {
        execute {
            listPresenter.getListItems(_listFilter.value!!).let { listItems ->
                viewState = (viewState as? ListReady)?.copy(listItems = listItems) ?: viewState
            }
        }
    }

    fun onListItemClicked(id: TransactionId) {
        postEvent(NavigateToDetailEvent(id))
    }

}
