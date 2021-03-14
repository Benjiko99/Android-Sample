package com.spiraclesoftware.androidsample.feature.transaction_list

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.domain.Result
import com.spiraclesoftware.androidsample.domain.entity.TransactionId
import com.spiraclesoftware.androidsample.feature.transaction_list.TransactionListFragmentDirections.Companion.toTransactionDetail
import com.spiraclesoftware.androidsample.feature.transaction_list.TransactionListViewModel.NavigateEvent
import com.spiraclesoftware.androidsample.feature.transaction_list.TransactionListViewModel.ShowLanguageChangeConfirmationEvent
import com.spiraclesoftware.androidsample.feature.transaction_list.TransactionListViewState.Content
import com.spiraclesoftware.androidsample.framework.Model
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionListViewModelTest : ViewModelTest() {

    @Test
    fun openTransactionDetail() {
        val presenter: TransactionListPresenter = mockk()
        val testSubject = TransactionListViewModel(presenter)

        testSubject.observeStateAndEvents { _, eventsObserver ->
            testSubject.openTransactionDetail(TransactionId("1"))

            eventsObserver.assertObserved(
                NavigateEvent(toTransactionDetail("1"))
            )
        }
    }

    @Test
    fun changeLanguage() {
        val presenter: TransactionListPresenter = mockk()
        val testSubject = TransactionListViewModel(presenter)

        testSubject.observeStateAndEvents { _, eventsObserver ->
            testSubject.changeLanguage()

            eventsObserver.assertObserved(
                ShowLanguageChangeConfirmationEvent
            )
        }
    }

    @Test
    fun confirmLanguageChange() {
        val presenter: TransactionListPresenter = mockk {
            justRun { toggleLanguageAndRestart() }
        }
        val testSubject = TransactionListViewModel(presenter)

        testSubject.confirmLanguageChange()
        verify { presenter.toggleLanguageAndRestart() }
    }

    @Test
    fun refreshData() = runBlockingTest {
        val presenter: TransactionListPresenter = mockk {
            coJustRun { refreshTransactions() }
        }
        val testSubject = TransactionListViewModel(presenter)

        testSubject.refreshData()
        coVerify { presenter.refreshTransactions() }
    }

    @Test
    fun retryOnError() = runBlockingTest {
        val presenter: TransactionListPresenter = mockk {
            coJustRun { refreshTransactions() }
        }
        val testSubject = TransactionListViewModel(presenter)

        testSubject.retryOnError()
        coVerify { presenter.refreshTransactions() }
    }

    @Test
    fun `Data is loaded correctly from presenter upon creation and leads to ready state`() = runBlockingTest {
        val presenter: TransactionListPresenter = mockk()
        val mockListModels = listOf(mockk<Model>())
        val mockFilterModel = mockk<FilterModel>()
        val mockEmptyState = mockk<EmptyState>()
        val contentModel = ContentModel(mockListModels, mockFilterModel, mockEmptyState)

        coEvery { presenter.flowContentModel(any()) } returns flowOf(Result.Success(contentModel))

        val testSubject = TransactionListViewModel(presenter)

        testSubject.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(
                Content(mockListModels, mockFilterModel, mockEmptyState)
            )
        }
    }

    @Test
    fun `Having no transactions leads to empty state`() = runBlockingTest {
        val presenter: TransactionListPresenter = mockk()
        val contentModel = ContentModel(emptyList(), mockk(), mockk())

        coEvery { presenter.flowContentModel(any()) } returns flowOf(Result.Success(contentModel))

        val testSubject = TransactionListViewModel(presenter)

        testSubject.observeStateAndEvents { stateObserver, _ ->
            val viewState = stateObserver.observed.first() as Content

            assertThat(viewState.listModels).isEmpty()
            assertThat(viewState.emptyState).isNotNull()
        }
    }

}
