package com.spiraclesoftware.androidsample.feature.transaction_detail

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import com.spiraclesoftware.androidsample.domain.entity.*
import com.spiraclesoftware.androidsample.epochDateTime
import com.spiraclesoftware.androidsample.feature.text_input.TextInputType
import com.spiraclesoftware.androidsample.feature.transaction_detail.TransactionDetailFragment.Companion.NOTE_INPUT_REQUEST_KEY
import com.spiraclesoftware.androidsample.feature.transaction_detail.TransactionDetailFragmentDirections.Companion.toCategorySelect
import com.spiraclesoftware.androidsample.feature.transaction_detail.TransactionDetailFragmentDirections.Companion.toTextInput
import com.spiraclesoftware.androidsample.feature.transaction_detail.TransactionDetailViewModel.*
import com.spiraclesoftware.androidsample.feature.transaction_detail.TransactionDetailViewState.Content
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.CardsPresenter
import com.spiraclesoftware.androidsample.formatter.MoneyFormat
import com.spiraclesoftware.androidsample.money
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionDetailViewModelTest : ViewModelTest() {

    companion object {
        private val MOCK_TRANSACTION = Transaction(
            TransactionId("1"),
            "Paypal *Steam",
            epochDateTime,
            money("49.99", "EUR"),
            TransferDirection.OUTGOING,
            TransactionCategory.ENTERTAINMENT,
            TransactionStatus.COMPLETED,
            TransactionStatusCode.SUCCESSFUL,
            emptyList(),
            "VISA **9400",
            "Half-Life: Alyx"
        )
    }

    @MockK
    lateinit var detailPresenter: TransactionDetailPresenter

    @MockK
    lateinit var cardsPresenter: CardsPresenter

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    private fun newTestSubject() =
        TransactionDetailViewModel(MOCK_TRANSACTION.id, detailPresenter, cardsPresenter)

    @Test
    fun onInit_produceViewState() = runBlockingTest {
        every { detailPresenter.flowTransactionById(any()) } returns flowOf(MOCK_TRANSACTION)
        every { cardsPresenter.getCards(any()) } returns emptyList()

        val viewModel = newTestSubject()
        viewModel.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(
                Content(
                    MOCK_TRANSACTION.name,
                    MOCK_TRANSACTION.processingDate,
                    MoneyFormat(MOCK_TRANSACTION.signedMoney).format(MOCK_TRANSACTION),
                    contributesToBalance = true,
                    isSuccessful = true,
                    MOCK_TRANSACTION.category,
                    emptyList()
                )
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
