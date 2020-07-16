package com.spiraclesoftware.androidsample.ui.transactionlist

import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.spiraclesoftware.androidsample.domain.model.TransactionId
import com.spiraclesoftware.androidsample.domain.model.TransactionListFilter
import com.spiraclesoftware.androidsample.domain.model.TransferDirectionFilter

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
        execute { loadData(ignoreCache = true) }
    }

    fun reload() {
        execute { loadData(ignoreCache = true) }
    }

    /**
     * @param ignoreCache whether to request latest from the network instead of using cached data
     */
    private suspend fun loadData(ignoreCache: Boolean = false) {
        viewState = Loading
        viewState = try {
            val listItems = listPresenter.getListItems(listFilter, ignoreCache)
            ListReady(
                listItems,
                listFilter
            )
        } catch (e: Exception) {
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
            listPresenter.getListItems(listFilter).let { listItems ->
                viewState = (viewState as? ListReady)?.copy(
                    listItems = listItems,
                    listFilter = listFilter
                ) ?: viewState
            }
        }
    }

}
