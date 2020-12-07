package com.spiraclesoftware.androidsample.ui.transactionlist

import androidx.navigation.NavDirections
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.spiraclesoftware.androidsample.domain.model.TransactionId
import com.spiraclesoftware.androidsample.domain.model.TransactionListFilter
import com.spiraclesoftware.androidsample.domain.model.TransferDirectionFilter
import com.spiraclesoftware.androidsample.ui.transactionlist.TransactionListFragmentDirections.Companion.toTransactionDetail
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionListViewModel(
    private val listPresenter: TransactionListPresenter
) : RainbowCakeViewModel<TransactionListViewState>(Loading) {

    data class NavigateEvent(val navDirections: NavDirections) : OneShotEvent

    object ShowLanguageChangeConfirmationEvent : OneShotEvent

    private var listFilterFlow = MutableStateFlow(TransactionListFilter())

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

    fun onLanguageChangeClicked() {
        postEvent(ShowLanguageChangeConfirmationEvent)
    }

    fun onListItemClicked(id: TransactionId) {
        postEvent(NavigateEvent(toTransactionDetail(id.value)))
    }

    fun setSearchQuery(query: String) = execute {
        if (listFilterFlow.value.searchQuery != query) {
            listFilterFlow.value = listFilterFlow.value.copy(searchQuery = query)
        }
    }

    fun setTransferDirectionFilter(directionFilter: TransferDirectionFilter) = execute {
        if (listFilterFlow.value.directionFilter != directionFilter) {
            listFilterFlow.value = listFilterFlow.value.copy(directionFilter = directionFilter)
        }
    }

}
