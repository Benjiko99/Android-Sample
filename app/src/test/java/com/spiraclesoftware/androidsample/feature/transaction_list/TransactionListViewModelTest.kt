package com.spiraclesoftware.androidsample.feature.transaction_list

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.domain.core.Result
import com.spiraclesoftware.androidsample.domain.entity.TransactionId
import com.spiraclesoftware.androidsample.feature.transaction_list.TransactionListViewModel.NavigateToProfileEvent
import com.spiraclesoftware.androidsample.feature.transaction_list.TransactionListViewModel.NavigateToTransactionDetailEvent
import com.spiraclesoftware.androidsample.feature.transaction_list.TransactionListViewState.Content
import com.spiraclesoftware.androidsample.framework.core.Model
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
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
    fun openProfile() {
        testSubject.observeStateAndEvents { _, eventsObserver ->
            testSubject.openProfile()

            eventsObserver.assertObserved(NavigateToProfileEvent)
        }
    }

    @Test
    fun openTransactionDetail() {
        testSubject.observeStateAndEvents { _, eventsObserver ->
            testSubject.openTransactionDetail(TransactionId("1"))

            eventsObserver.assertObserved(
                NavigateToTransactionDetailEvent("1")
            )
        }
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
