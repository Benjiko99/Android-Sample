package com.spiraclesoftware.androidsample.feature.transaction_detail

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import com.spiraclesoftware.androidsample.domain.Result
import com.spiraclesoftware.androidsample.domain.entity.TransactionCategory
import com.spiraclesoftware.androidsample.domain.entity.TransactionId
import com.spiraclesoftware.androidsample.feature.text_input.TextInputType
import com.spiraclesoftware.androidsample.feature.transaction_detail.TransactionDetailFragment.Companion.NOTE_INPUT_REQUEST_KEY
import com.spiraclesoftware.androidsample.feature.transaction_detail.TransactionDetailFragmentDirections.Companion.toCategorySelect
import com.spiraclesoftware.androidsample.feature.transaction_detail.TransactionDetailFragmentDirections.Companion.toTextInput
import com.spiraclesoftware.androidsample.feature.transaction_detail.TransactionDetailViewModel.*
import com.spiraclesoftware.androidsample.feature.transaction_detail.TransactionDetailViewState.Content
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionDetailViewModelTest : ViewModelTest() {

    @MockK
    lateinit var detailPresenter: TransactionDetailPresenter

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    private fun newTestSubject() =
        TransactionDetailViewModel(TransactionId("1"), detailPresenter)

    @Test
    fun onInit_produceViewState() = runBlockingTest {
        val detailModel = mockk<DetailModel>()

        every { detailPresenter.flowDetailModel(any(), any()) } returns flowOf(Result.Success(detailModel))
        every { detailPresenter.flowTransactionById(any()) } returns flowOf(mockk())

        val viewModel = newTestSubject()
        viewModel.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(
                Content(detailModel)
            )
        }
    }

    @Test
    fun `Clicking change note card action produces navigate to note input event`() = runBlockingTest {
        val currentNote = "hello world"

        coEvery { detailPresenter.getNote(any()) } returns currentNote

        val viewModel = newTestSubject()
        viewModel.observeStateAndEvents { _, eventsObserver ->
            viewModel.onChangeNote()

            val navDirections = toTextInput(
                TextInputType.NOTE,
                NOTE_INPUT_REQUEST_KEY,
                initialValue = currentNote
            )

            eventsObserver.assertObserved(
                NavigateEvent(navDirections)
            )
        }
    }

    @Test
    fun `Clicking category select card action produces navigate to category select event`() = runBlockingTest {
        val currentCategory = TransactionCategory.ENTERTAINMENT

        coEvery { detailPresenter.getCategory(any()) } returns currentCategory

        val viewModel = newTestSubject()
        viewModel.observeStateAndEvents { _, eventsObserver ->
            viewModel.onSelectCategory()

            val navDirections = toCategorySelect(
                "1",
                currentCategory
            )

            eventsObserver.assertObserved(
                NavigateEvent(navDirections)
            )
        }
    }

    @Test
    fun onOpenCardDetail_postEvent_NavigateToCardDetailEvent() = runBlockingTest {
        val viewModel = newTestSubject()
        viewModel.observeStateAndEvents { _, eventsObserver ->
            viewModel.onOpenCardDetail()

            eventsObserver.assertObserved(NavigateToCardDetailEvent)
        }
    }

    @Test
    fun onDownloadStatement_postEvent_DownloadStatementEvent() = runBlockingTest {
        val viewModel = newTestSubject()
        viewModel.observeStateAndEvents { _, eventsObserver ->
            viewModel.onDownloadStatement()

            eventsObserver.assertObserved(DownloadStatementEvent)
        }
    }

}
