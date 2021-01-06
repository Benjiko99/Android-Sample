package com.spiraclesoftware.androidsample.ui.transactionlist

import androidx.navigation.NavDirections
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.domain.model.TransactionId
import com.spiraclesoftware.androidsample.domain.model.TransactionListFilter
import com.spiraclesoftware.androidsample.domain.model.TransferDirectionFilter
import com.spiraclesoftware.androidsample.ui.shared.PresenterException
import com.spiraclesoftware.androidsample.ui.transactionlist.TransactionListFragmentDirections.Companion.toTransactionDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect

class TransactionListViewModel(
    private val presenter: TransactionListPresenter
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
        presenter.flowFilteredTransactions(listFilterFlow).collect { transactions ->
            viewState = try {
                tryGetContent(transactions)
            } catch (e: PresenterException) {
                Error(e.message)
            }
        }

    private suspend fun tryGetContent(transactions: List<Transaction>): Content {
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

    private suspend fun fetchTransactions() {
        try {
            presenter.fetchTransactions()
        } catch (e: PresenterException) {
            viewState = Error(e.message)
        }
    }

    fun refreshTransactions() = executeNonBlocking {
        viewState = Loading
        fetchTransactions()
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
