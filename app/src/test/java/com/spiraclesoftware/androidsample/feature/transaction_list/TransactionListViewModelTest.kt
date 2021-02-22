package com.spiraclesoftware.androidsample.feature.transaction_list

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.domain.Result
import com.spiraclesoftware.androidsample.feature.transaction_list.TransactionListViewState.Content
import com.spiraclesoftware.androidsample.framework.Model
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionListViewModelTest : ViewModelTest() {

    @Test
    fun `Data is loaded correctly from presenter upon creation and leads to ready state`() = runBlockingTest {
        val presenter: TransactionListPresenter = mockk()
        val models = listOf(mockk<Model>())

        coEvery { presenter.flowListModels(any()) } returns flowOf(Result.Success(models))

        val vm = TransactionListViewModel(presenter)

        vm.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(
                Content(
                    models,
                    TransferDirectionFilter.ALL
                )
            )
        }
    }

    @Test
    fun `Having no transactions leads to empty state`() = runBlockingTest {
        val presenter: TransactionListPresenter = mockk()
        coEvery { presenter.flowListModels(any()) } returns flowOf(Result.Success(emptyList()))

        val vm = TransactionListViewModel(presenter)

        vm.observeStateAndEvents { stateObserver, _ ->
            val viewState = stateObserver.observed.first() as Content

            assertThat(viewState.listModels).isEmpty()
            assertThat(viewState.emptyState).isNotNull()
        }
    }

}
