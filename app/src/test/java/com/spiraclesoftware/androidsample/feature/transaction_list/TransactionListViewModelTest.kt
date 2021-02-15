package com.spiraclesoftware.androidsample.feature.transaction_list

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.spiraclesoftware.androidsample.domain.Result
import com.spiraclesoftware.androidsample.domain.entity.*
import com.spiraclesoftware.androidsample.epochDateTime
import com.spiraclesoftware.androidsample.feature.transaction_list.TransactionListViewState.Content
import com.spiraclesoftware.androidsample.framework.Model
import com.spiraclesoftware.androidsample.money
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionListViewModelTest : ViewModelTest() {

    companion object {
        private val MOCK_TRANSACTIONS = listOf(
            Transaction(
                TransactionId("1"),
                "Paypal *Steam",
                epochDateTime,
                money("49.99", "EUR"),
                TransferDirection.OUTGOING,
                TransactionCategory.ENTERTAINMENT,
                TransactionStatus.COMPLETED,
                TransactionStatusCode.SUCCESSFUL,
            ),
        )

        private val MOCK_LIST_MODELS = listOf(mock<Model>())
    }

    @Test
    fun `Data is loaded correctly from presenter upon creation and leads to ready state`() = runBlockingTest {
        val presenter: TransactionListPresenter = mock()
        whenever(presenter.flowTransactions(any())) doReturn flowOf(Result.Success(MOCK_TRANSACTIONS))
        whenever(presenter.getListModels(any())) doReturn MOCK_LIST_MODELS

        val vm = TransactionListViewModel(presenter)

        vm.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(
                Content(
                    MOCK_LIST_MODELS,
                    TransferDirectionFilter.ALL
                )
            )
        }
    }

    @Test
    fun `Having no transactions leads to empty state`() = runBlockingTest {
        val presenter: TransactionListPresenter = mock()
        whenever(presenter.flowTransactions(any())) doReturn flowOf(Result.Success(emptyList()))
        whenever(presenter.getListModels(any())) doReturn emptyList()

        val vm = TransactionListViewModel(presenter)

        vm.observeStateAndEvents { stateObserver, _ ->
            val viewState = stateObserver.observed.first() as Content

            assert(viewState.listModels.isEmpty())
            assert(viewState.emptyState != null)
        }
    }

}
