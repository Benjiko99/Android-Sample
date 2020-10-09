package com.spiraclesoftware.androidsample.ui.transactionlist

import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.spiraclesoftware.androidsample.domain.model.TransactionId
import com.spiraclesoftware.androidsample.domain.model.TransactionListFilter
import com.spiraclesoftware.androidsample.domain.model.TransferDirectionFilter
import com.spiraclesoftware.androidsample.domain.model.TransferDirectionFilter.ALL
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@ExperimentalCoroutinesApi
class TransactionListViewModel(
    private val listPresenter: TransactionListPresenter
) : RainbowCakeViewModel<TransactionListViewState>(Loading) {

    data class NavigateToDetailEvent(val id: TransactionId) : OneShotEvent

    object ShowLanguageChangeDialogEvent : OneShotEvent

    private var listFilterFlow = MutableStateFlow(TransactionListFilter(ALL))

    init {
        executeNonBlocking {
            fetchTransactions()
            collectTransactions()
        }
    }

    suspend fun collectTransactions() =
        listPresenter.flowFilteredTransactions(listFilterFlow).collect { transactions ->
            viewState = try {
                ListReady(
                    listItems = listPresenter.getListItems(transactions),
                    listFilter = listFilterFlow.value
                )
            } catch (e: Exception) {
                Timber.e(e)
                Error
            }
        }

    private suspend fun fetchTransactions() {
        try {
            listPresenter.fetchTransactions()
        } catch (e: Exception) {
            Timber.e(e)
            viewState = Error
        }
    }

    fun refreshTransactions() = executeNonBlocking {
        viewState = Loading
        fetchTransactions()
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

    fun setTransferDirectionFilter(directionFilter: TransferDirectionFilter) = execute {
        if (listFilterFlow.value.directionFilter != directionFilter) {
            listFilterFlow.value = listFilterFlow.value.copy(directionFilter = directionFilter)
        }
    }

}
