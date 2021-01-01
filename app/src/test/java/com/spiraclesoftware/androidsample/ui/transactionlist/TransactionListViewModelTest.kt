package com.spiraclesoftware.androidsample.ui.transactionlist

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.spiraclesoftware.androidsample.domain.model.*
import com.spiraclesoftware.androidsample.epochDateTime
import com.spiraclesoftware.androidsample.money
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import java.io.IOException

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

        private val MOCK_LIST_ITEMS = MOCK_TRANSACTIONS.map(::TransactionItem)
    }

    @Test
    fun `Data is loaded correctly from presenter upon creation and leads to ready state`() = runBlockingTest {
        val presenter: TransactionListPresenter = mock()
        whenever(presenter.flowFilteredTransactions(any())) doReturn flowOf(MOCK_TRANSACTIONS)
        whenever(presenter.getListItems(any())) doReturn MOCK_LIST_ITEMS

        val vm = TransactionListViewModel(presenter)

        vm.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(
                ListReady(
                    MOCK_LIST_ITEMS,
                    TransferDirectionFilter.ALL
                )
            )
        }
    }

    @Test
    fun `Presenter error when loading data leads to error state`() = runBlockingTest {
        val presenter: TransactionListPresenter = mock()
        whenever(presenter.flowFilteredTransactions(any())).thenReturn(flowOf(MOCK_TRANSACTIONS))
        whenever(presenter.getListItems(any())).thenThrow()

        val vm = TransactionListViewModel(presenter)

        vm.observeStateAndEvents { stateObserver, _ ->
            launch {
                vm.collectTransactions()
                stateObserver.assertObserved(
                    Error
                )
            }
        }
    }

    @Test
    fun `Retrying after error loads correctly into ready state`() = runBlockingTest {
        val presenter: TransactionListPresenter = mock()
        whenever(presenter.flowFilteredTransactions(any())) doReturn flowOf(MOCK_TRANSACTIONS)
        var invocations = 0
        whenever(presenter.getListItems(any())).thenAnswer {
            when (invocations++) {
                0 -> throw IOException()
                else -> MOCK_LIST_ITEMS
            }
        }

        val vm = TransactionListViewModel(presenter)

        vm.observeStateAndEvents { stateObserver, _ ->
            launch {
                vm.collectTransactions()
                vm.refreshTransactions()

                stateObserver.assertObserved(
                    Error,
                    ListReady(
                        MOCK_LIST_ITEMS,
                        TransferDirectionFilter.ALL
                    ),
                    Loading
                )
            }
        }
    }

    @Test
    fun `Having no transactions leads to empty state`() = runBlockingTest {
        val presenter: TransactionListPresenter = mock()
        whenever(presenter.flowFilteredTransactions(any())) doReturn flowOf(emptyList())
        whenever(presenter.getListItems(any())) doReturn emptyList()

        val vm = TransactionListViewModel(presenter)

        vm.observeStateAndEvents { stateObserver, _ ->
            val viewState = stateObserver.observed.first() as ListReady

            assert(viewState.listItems.isEmpty())
            assert(viewState.emptyState != null)
        }
    }

}
