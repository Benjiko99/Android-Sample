package com.spiraclesoftware.androidsample.feature.transaction_list

import androidx.navigation.NavDirections
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.domain.Result
import com.spiraclesoftware.androidsample.domain.entity.Transaction
import com.spiraclesoftware.androidsample.domain.entity.TransactionId
import com.spiraclesoftware.androidsample.feature.transaction_list.TransactionListFragmentDirections.Companion.toTransactionDetail
import com.spiraclesoftware.androidsample.feature.transaction_list.TransactionListViewState.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect

class TransactionListViewModel(
    private val presenter: TransactionListPresenter
) : RainbowCakeViewModel<TransactionListViewState>(Loading) {

    data class NavigateEvent(val navDirections: NavDirections) : OneShotEvent

    object ShowLanguageChangeConfirmationEvent : OneShotEvent

    private var listFilterFlow = MutableStateFlow(TransactionListFilter())

    init {
        produceViewStateFromDataFlow()
        refreshTransactions()
    }

    fun produceViewStateFromDataFlow() = executeNonBlocking {
        presenter.flowTransactions(listFilterFlow).collect { result ->
            viewState = when (result) {
                is Result.Loading -> Loading
                is Result.Success -> getContent(result.data)
                is Result.Error -> Error(result.exception.message)
                else -> throw IllegalStateException()
            }
        }
    }

    private suspend fun getContent(transactions: List<Transaction>): Content {
        val listItems = presenter.getListItems(transactions)
        val listFilter = listFilterFlow.value
        var emptyState: EmptyState? = null

        if (listItems.isEmpty()) {
            emptyState = if (listFilter.isActive()) {
                EmptyState(
                    image = R.drawable.ic_empty_search_results,
                    caption = R.string.empty_state__no_results__caption,
                    message = R.string.empty_state__no_results__message
                )
            } else {
                EmptyState(
                    caption = R.string.empty_state__no_transactions__caption,
                    message = R.string.empty_state__no_transactions__message
                )
            }
        }
        return Content(listItems, listFilter.directionFilter, emptyState)
    }

    fun refreshTransactions() = executeNonBlocking {
        presenter.refreshTransactions()
    }

    fun toggleLanguage() {
        presenter.toggleLanguageAndRestart()
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
