package com.spiraclesoftware.androidsample.feature.transaction_list

import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.spiraclesoftware.androidsample.domain.core.Result
import com.spiraclesoftware.androidsample.domain.entity.TransactionId
import com.spiraclesoftware.androidsample.domain.entity.TransactionsFilter
import com.spiraclesoftware.androidsample.domain.entity.TransferDirectionFilter
import com.spiraclesoftware.androidsample.feature.transaction_list.TransactionListViewState.*
import kotlinx.coroutines.flow.MutableStateFlow

class TransactionListViewModel(
    private val presenter: TransactionListPresenter
) : RainbowCakeViewModel<TransactionListViewState>(Loading) {

    object NavigateToSettingsEvent : OneShotEvent

    object NavigateToProfileEvent : OneShotEvent

    data class NavigateToTransactionDetailEvent(val id: String) : OneShotEvent

    private var filterFlow = MutableStateFlow(TransactionsFilter())

    init {
        produceViewState()
        refreshTransactions()
    }

    fun openSettings() {
        postEvent(NavigateToSettingsEvent)
    }

    fun openProfile() {
        postEvent(NavigateToProfileEvent)
    }

    fun openTransactionDetail(id: TransactionId) {
        postEvent(NavigateToTransactionDetailEvent(id.value))
    }

    fun filterByTransferDirection(filter: TransferDirectionFilter) {
        setTransferDirectionFilter(filter)
    }

    fun filterByQuery(query: String) {
        setSearchQuery(query)
    }

    fun refreshData() {
        refreshTransactions()
    }

    fun retryOnError() {
        refreshTransactions()
    }

    fun getFilterStringIds(): List<Int> {
        return presenter.getFilterStringIds()
    }

    private fun produceViewState() = executeNonBlocking {
        presenter.flowContentModel(filterFlow).collect { result ->
            viewState = when (result) {
                is Result.Loading -> Loading
                is Result.Success -> with(result.data) {
                    Content(listModels, filterModel, emptyState)
                }
                is Result.Error -> Error(result.exception.message)
                else -> throw IllegalStateException()
            }
        }
    }

    private fun refreshTransactions() = executeNonBlocking {
        presenter.refreshTransactions()
    }

    private fun setSearchQuery(query: String) = execute {
        if (filterFlow.value.searchQuery != query) {
            filterFlow.value = filterFlow.value.copy(searchQuery = query)
        }
    }

    private fun setTransferDirectionFilter(directionFilter: TransferDirectionFilter) = execute {
        if (filterFlow.value.directionFilter != directionFilter) {
            filterFlow.value = filterFlow.value.copy(directionFilter = directionFilter)
        }
    }

}
