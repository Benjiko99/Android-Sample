package com.spiraclesoftware.androidsample.feature.transaction_list

import androidx.navigation.NavDirections
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.spiraclesoftware.androidsample.domain.Result
import com.spiraclesoftware.androidsample.domain.entity.TransactionId
import com.spiraclesoftware.androidsample.feature.transaction_list.TransactionListFragmentDirections.Companion.toTransactionDetail
import com.spiraclesoftware.androidsample.feature.transaction_list.TransactionListViewState.*
import com.spiraclesoftware.androidsample.framework.Model
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect

class TransactionListViewModel(
    private val presenter: TransactionListPresenter
) : RainbowCakeViewModel<TransactionListViewState>(Loading) {

    data class ViewData(
        val listModels: List<Model>,
        val listFilter: TransactionListFilter,
        val emptyState: EmptyState?,
    )

    data class NavigateEvent(val navDirections: NavDirections) : OneShotEvent

    object ShowLanguageChangeConfirmationEvent : OneShotEvent

    private var listFilterFlow = MutableStateFlow(TransactionListFilter())

    init {
        produceViewStateFromDataFlow()
        refreshTransactions()
    }

    fun openTransactionDetail(id: TransactionId) {
        postEvent(NavigateEvent(toTransactionDetail(id.value)))
    }

    fun filterByTransferDirection(filter: TransferDirectionFilter) {
        setTransferDirectionFilter(filter)
    }

    fun filterByQuery(query: String) {
        setSearchQuery(query)
    }

    fun changeLanguage() {
        postEvent(ShowLanguageChangeConfirmationEvent)
    }

    fun confirmLanguageChange() {
        toggleLanguage()
    }

    fun refreshData() {
        refreshTransactions()
    }

    fun retryOnError() {
        refreshTransactions()
    }

    private fun produceViewStateFromDataFlow() = executeNonBlocking {
        presenter.flowViewData(listFilterFlow).collect { result ->
            viewState = when (result) {
                is Result.Loading -> Loading
                is Result.Success -> getContent(result.data)
                is Result.Error -> Error(result.exception.message)
                else -> throw IllegalStateException()
            }
        }
    }

    private fun getContent(data: ViewData): Content {
        val directionFilter = listFilterFlow.value.directionFilter
        return Content(data.listModels, directionFilter, data.emptyState)
    }

    private fun refreshTransactions() = executeNonBlocking {
        presenter.refreshTransactions()
    }

    private fun toggleLanguage() {
        presenter.toggleLanguageAndRestart()
    }

    private fun setSearchQuery(query: String) = execute {
        if (listFilterFlow.value.searchQuery != query) {
            listFilterFlow.value = listFilterFlow.value.copy(searchQuery = query)
        }
    }

    private fun setTransferDirectionFilter(directionFilter: TransferDirectionFilter) = execute {
        if (listFilterFlow.value.directionFilter != directionFilter) {
            listFilterFlow.value = listFilterFlow.value.copy(directionFilter = directionFilter)
        }
    }

}
