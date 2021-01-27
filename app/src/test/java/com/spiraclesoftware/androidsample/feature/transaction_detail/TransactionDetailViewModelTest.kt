package com.spiraclesoftware.androidsample.feature.transaction_detail

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import com.spiraclesoftware.androidsample.domain.entity.*
import com.spiraclesoftware.androidsample.epochDateTime
import com.spiraclesoftware.androidsample.feature.text_input.TextInputType
import com.spiraclesoftware.androidsample.feature.transaction_detail.TransactionDetailFragment.Companion.NOTE_INPUT_REQUEST_KEY
import com.spiraclesoftware.androidsample.feature.transaction_detail.TransactionDetailFragmentDirections.Companion.toCategorySelect
import com.spiraclesoftware.androidsample.feature.transaction_detail.TransactionDetailFragmentDirections.Companion.toTextInput
import com.spiraclesoftware.androidsample.feature.transaction_detail.TransactionDetailViewModel.*
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.CardsPresenter
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.items.ValuePairCardItem
import com.spiraclesoftware.androidsample.formatter.MoneyFormat
import com.spiraclesoftware.androidsample.money
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

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

        private val MOCK_CARD_ITEMS = emptyList<ValuePairCardItem>()
    }

    @Mock
    private lateinit var detailPresenter: TransactionDetailPresenter

    @Mock
    private lateinit var cardsPresenter: CardsPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    private fun newTestSubject() =
        TransactionDetailViewModel(MOCK_TRANSACTION.id, detailPresenter, cardsPresenter)

    @Test
    fun onInit_produceViewState() = runBlockingTest {
        whenever(detailPresenter.flowTransactionById(any())) doReturn flowOf(MOCK_TRANSACTION)

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
                    MOCK_CARD_ITEMS
                )
            )
        }
    }

    @Test
    fun `Clicking change note card action produces navigate to note input event`() = runBlockingTest {
        val currentNote = "hello world"

        whenever(detailPresenter.getNote(any())).thenReturn(currentNote)

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

        whenever(detailPresenter.getCategory(any())).thenReturn(currentCategory)

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
