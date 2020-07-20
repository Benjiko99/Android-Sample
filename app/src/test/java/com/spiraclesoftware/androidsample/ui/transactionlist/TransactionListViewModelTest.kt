package com.spiraclesoftware.androidsample.ui.transactionlist

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.spiraclesoftware.androidsample.TestData
import com.spiraclesoftware.androidsample.domain.model.TransactionListFilter
import com.spiraclesoftware.androidsample.domain.model.TransferDirectionFilter.ALL
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionListViewModelTest : ViewModelTest() {

    companion object {
        private val MOCK_TRANSACTIONS = TestData.transactions
        private val MOCK_LIST_ITEMS = MOCK_TRANSACTIONS.map(::TransactionItem)
    }

    @Test
    fun `Data is loaded correctly from presenter upon creation and leads to ready state`() = runBlockingTest {
        val presenter: TransactionListPresenter = mock()
        whenever(presenter.getTransactions(any(), any())) doReturn MOCK_TRANSACTIONS
        whenever(presenter.getListItems(any())) doReturn MOCK_LIST_ITEMS

        val vm = TransactionListViewModel(presenter)

        vm.observeStateAndEvents { stateObserver, eventsObserver ->
            stateObserver.assertObserved(
                ListReady(
                    MOCK_LIST_ITEMS,
                    TransactionListFilter(ALL)
                )
            )
        }
    }

    @Test
    fun `Presenter error when loading data leads to error state`() = runBlockingTest {
        val presenter: TransactionListPresenter = mock()
        whenever(presenter.getListItems(any())).thenAnswer {
            throw IOException()
        }

        val vm = TransactionListViewModel(presenter)

        vm.observeStateAndEvents { stateObserver, eventsObserver ->
            stateObserver.assertObserved(Error)
        }
    }

    @Test
    fun `Retrying after error loads correctly into ready state`() = runBlockingTest {
        val presenter: TransactionListPresenter = mock()
        whenever(presenter.getTransactions(any(), any())).doReturn(MOCK_TRANSACTIONS)
        var invocations = 0
        whenever(presenter.getListItems(any())).thenAnswer {
            when (invocations++) {
                0 -> throw IOException()
                else -> MOCK_LIST_ITEMS
            }
        }

        val vm = TransactionListViewModel(presenter)

        vm.observeStateAndEvents { stateObserver, eventsObserver ->
            vm.reload()

            stateObserver.assertObserved(
                Error,
                Loading,
                ListReady(
                    MOCK_LIST_ITEMS,
                    TransactionListFilter(ALL)
                )
            )
        }
    }

}
