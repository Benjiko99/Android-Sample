package com.spiraclesoftware.androidsample.ui.transactionlist

import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.spiraclesoftware.androidsample.domain.model.TransactionId
import com.spiraclesoftware.androidsample.domain.model.TransactionListFilter
import com.spiraclesoftware.androidsample.domain.model.TransferDirectionFilter
import timber.log.Timber

class TransactionListViewModel(
    private val listPresenter: TransactionListPresenter
) : RainbowCakeViewModel<TransactionListViewState>(Loading) {

    data class NavigateToDetailEvent(val id: TransactionId) : OneShotEvent
    object ShowLanguageChangeDialogEvent : OneShotEvent

    private var listFilter = TransactionListFilter(TransferDirectionFilter.ALL)

    init {
        // Since we don't want offline support but do have a local database
        // for the purposes of showcasing storing data in it,
        // we'll simply ignore the cache to get the latest data on startup.
        execute { loadData(forceFetch = true) }
    }

    fun reload() {
        execute { loadData(forceFetch = true) }
    }

    private suspend fun loadData(forceFetch: Boolean) {
        viewState = Loading
        viewState = try {
            val transactions = listPresenter.getTransactions(listFilter, forceFetch)
            val listItems = listPresenter.getListItems(transactions)

            Timber.d("Loaded data for list of transactions; forceFetch=$forceFetch")
            ListReady(
                listItems,
                listFilter
            )
        } catch (e: Exception) {
            Timber.e(e)
            NetworkError
        }
    }

    fun toggleLanguage() {
        listPresenter.toggleLanguageAndRestart()
    }

    fun showLanguageChangeDialog() {
        postEvent(ShowLanguageChangeDialogEvent)
    }

    fun onListItemClicked(id: TransactionId) {
        postEvent(NavigateToDetailEvent(id))
    }

    fun setTransferDirectionFilter(directionFilter: TransferDirectionFilter) {
        if (listFilter.transferDirectionFilter != directionFilter) {
            listFilter = listFilter.copy(transferDirectionFilter = directionFilter)

            updateListFilter()
        }
    }

    /** Updates the [viewState] with list items that are using the latest filter. **/
    private fun updateListFilter() {
        execute {
            val transactions = listPresenter.getTransactions(listFilter)
            val listItems = listPresenter.getListItems(transactions)

            (viewState as? ListReady)?.let {
                viewState = it.copy(
                    listItems = listItems,
                    listFilter = listFilter
                )
            }
        }
    }

}
