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

    private val presenter: TransactionListPresenter = mockk()

    private val testSubject by lazy {
        TransactionListViewModel(presenter)
    }

    @Test
    fun openTransactionDetail() {
        testSubject.observeStateAndEvents { _, eventsObserver ->
            testSubject.openTransactionDetail(TransactionId("1"))

            eventsObserver.assertObserved(
                NavigateEvent(toTransactionDetail("1"))
            )
        }
    }

    @Test
    fun changeLanguage() {
        testSubject.observeStateAndEvents { _, eventsObserver ->
            testSubject.changeLanguage()

            eventsObserver.assertObserved(
                ShowLanguageChangeConfirmationEvent
            )
        }
    }

    @Test
    fun confirmLanguageChange() {
        justRun { presenter.toggleLanguageAndRestart() }

        testSubject.confirmLanguageChange()

        verify { presenter.toggleLanguageAndRestart() }
    }

    @Test
    fun refreshData() = runBlockingTest {
        coJustRun { presenter.refreshTransactions() }

        testSubject.refreshData()

        coVerify { presenter.refreshTransactions() }
    }

    @Test
    fun retryOnError() = runBlockingTest {
        coJustRun { presenter.refreshTransactions() }

        testSubject.retryOnError()

        coVerify { presenter.refreshTransactions() }
    }

    @Test
    fun produceContentState() = runBlockingTest {
        val mockListModels = listOf(mockk<Model>())
        val mockFilterModel = mockk<FilterModel>()
        val mockEmptyState = mockk<EmptyState>()
        val contentModel = ContentModel(mockListModels, mockFilterModel, mockEmptyState)

        coEvery { presenter.flowContentModel(any()) } returns flowOf(Result.Success(contentModel))

        testSubject.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(
                Content(mockListModels, mockFilterModel, mockEmptyState)
            )
        }
    }

    @Test
    fun produceEmptyState_whenContentModelIsEmpty() = runBlockingTest {
        val contentModel = ContentModel(emptyList(), mockk(), mockk())

        coEvery { presenter.flowContentModel(any()) } returns flowOf(Result.Success(contentModel))

        testSubject.observeStateAndEvents { stateObserver, _ ->
            val viewState = stateObserver.observed.first() as Content

            assertThat(viewState.listModels).isEmpty()
            assertThat(viewState.emptyState).isNotNull()
        }
    }

}
