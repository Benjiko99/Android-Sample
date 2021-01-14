package com.spiraclesoftware.androidsample.transaction_detail

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import com.spiraclesoftware.androidsample.domain.model.*
import com.spiraclesoftware.androidsample.epochDateTime
import com.spiraclesoftware.androidsample.money
import com.spiraclesoftware.androidsample.shared.MoneyFormat
import com.spiraclesoftware.androidsample.text_input.TextInputType
import com.spiraclesoftware.androidsample.transaction_detail.TransactionDetailFragment.Companion.NOTE_INPUT_REQUEST_KEY
import com.spiraclesoftware.androidsample.transaction_detail.TransactionDetailFragmentDirections.Companion.toCategorySelect
import com.spiraclesoftware.androidsample.transaction_detail.TransactionDetailFragmentDirections.Companion.toTextInput
import com.spiraclesoftware.androidsample.transaction_detail.TransactionDetailViewModel.*
import com.spiraclesoftware.androidsample.transaction_detail.cards.CardsPresenter
import com.spiraclesoftware.androidsample.transaction_detail.cards.items.ValuePairCardItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Ignore
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

    @Test
    fun `Data is loaded correctly from presenter and leads to ready state`() = runBlockingTest {
        whenever(detailPresenter.flowTransactionById(any())) doReturn flowOf(MOCK_TRANSACTION)

        val vm = TransactionDetailViewModel(MOCK_TRANSACTION.id, detailPresenter, cardsPresenter)

        vm.observeStateAndEvents { stateObserver, _ ->
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
    @Ignore("Currently we don't load any data through the presenter so can't test this")
    fun `Presenter error when loading data leads to error state`() = runBlockingTest {
        whenever(detailPresenter.flowTransactionById(any())).thenReturn(flowOf(MOCK_TRANSACTION))
        //whenever(cardsPresenter.getCardItems(any(), any())).thenThrow()

        val vm = TransactionDetailViewModel(MOCK_TRANSACTION.id, detailPresenter, cardsPresenter)

        vm.observeStateAndEvents { stateObserver, _ ->
            launch {
                vm.collectScreenData()
                stateObserver.assertObserved(
                    Loading,
                    Error
                )
            }
        }
    }

    @Test
    fun `Clicking change note card action produces navigate to note input event`() = runBlockingTest {
        val currentNote = "hello world"

        whenever(detailPresenter.getNote(any())).thenReturn(currentNote)

        val vm = TransactionDetailViewModel(MOCK_TRANSACTION.id, detailPresenter, cardsPresenter)

        vm.observeStateAndEvents { _, eventsObserver ->
            vm.onChangeNote()

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

        val vm = TransactionDetailViewModel(MOCK_TRANSACTION.id, detailPresenter, cardsPresenter)

        vm.observeStateAndEvents { _, eventsObserver ->
            vm.onSelectCategory()

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
    fun `Clicking card description card action produces navigate to card detail event`() = runBlockingTest {
        val vm = TransactionDetailViewModel(MOCK_TRANSACTION.id, detailPresenter, cardsPresenter)

        vm.observeStateAndEvents { _, eventsObserver ->
            vm.onOpenCardDetail()

            eventsObserver.assertObserved(
                NavigateToCardDetailEvent
            )
        }
    }

    @Test
    fun `Clicking download statement card action produces download statement event`() = runBlockingTest {
        val vm = TransactionDetailViewModel(MOCK_TRANSACTION.id, detailPresenter, cardsPresenter)

        vm.observeStateAndEvents { _, eventsObserver ->
            vm.onDownloadStatement()

            eventsObserver.assertObserved(
                DownloadStatementEvent
            )
        }
    }

}
