package com.spiraclesoftware.androidsample.ui.transactionlist

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.spiraclesoftware.androidsample.TestData
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
    fun `Data is loaded correctly from presenter upon creation`() = runBlockingTest {
        val presenter: TransactionListPresenter = mock()
        whenever(presenter.getListItems(any(), any())).doReturn(MOCK_LIST_ITEMS)

        val vm = TransactionListViewModel(presenter)

        vm.observeStateAndEvents { stateObserver, eventsObserver ->
            stateObserver.assertObserved(
                ListReady(MOCK_LIST_ITEMS)
            )
        }
    }

    @Test
    fun `Presenter error leads to error state upon creation`() = runBlockingTest {
        val presenter: TransactionListPresenter = mock()
        whenever(presenter.getListItems(any(), any())).thenAnswer {
            throw IOException("Network error")
        }

        val vm = TransactionListViewModel(presenter)

        vm.observeStateAndEvents { stateObserver, eventsObserver ->
            stateObserver.assertObserved(NetworkError)
        }
    }

    @Test
    fun `Reload after error can load items correctly`() = runBlockingTest {
        val presenter: TransactionListPresenter = mock()
        var invocations = 0
        whenever(presenter.getListItems(any(), any())).thenAnswer {
            when (invocations++) {
                0 -> throw IOException("Network error")
                else -> MOCK_LIST_ITEMS
            }
        }

        val vm = TransactionListViewModel(presenter)

        vm.observeStateAndEvents { stateObserver, eventsObserver ->
            vm.reload()

            stateObserver.assertObserved(
                NetworkError,
                Loading,
                ListReady(MOCK_LIST_ITEMS)
            )
        }
    }

}
