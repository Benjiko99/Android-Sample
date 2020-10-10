package com.spiraclesoftware.androidsample.ui.transactiondetail

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import com.spiraclesoftware.androidsample.TestData
import com.spiraclesoftware.androidsample.domain.model.TransactionCategory
import com.spiraclesoftware.androidsample.ui.shared.MoneyFormat
import com.spiraclesoftware.androidsample.ui.textinput.TextInputType
import com.spiraclesoftware.androidsample.ui.transactiondetail.TransactionDetailFragment.Companion.NOTE_INPUT_REQUEST_KEY
import com.spiraclesoftware.androidsample.ui.transactiondetail.TransactionDetailFragmentDirections.Companion.toCategorySelect
import com.spiraclesoftware.androidsample.ui.transactiondetail.TransactionDetailFragmentDirections.Companion.toTextInput
import com.spiraclesoftware.androidsample.ui.transactiondetail.TransactionDetailViewModel.*
import com.spiraclesoftware.androidsample.ui.transactiondetail.cards.CardsPresenter
import com.spiraclesoftware.androidsample.ui.transactiondetail.cards.items.ValuePairCardItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionDetailViewModelTest : ViewModelTest() {

    companion object {
        private val MOCK_TRANSACTION = TestData.transactions[0]

        private val MOCK_TRANSACTION_ID = MOCK_TRANSACTION.id

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
        val contributesToBalance = true
        val isSuccessful = true

        whenever(detailPresenter.flowTransactionById(any())) doReturn flowOf(MOCK_TRANSACTION)
        whenever(cardsPresenter.getCardItems(any(), any())) doReturn MOCK_CARD_ITEMS
        whenever(detailPresenter.contributesToBalance(any())) doReturn contributesToBalance
        whenever(detailPresenter.isSuccessful(any())) doReturn isSuccessful

        val vm = TransactionDetailViewModel(MOCK_TRANSACTION_ID, detailPresenter, cardsPresenter)

        vm.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(
                DetailReady(
                    MOCK_TRANSACTION.name,
                    MOCK_TRANSACTION.processingDate,
                    MoneyFormat(MOCK_TRANSACTION.signedMoney).format(MOCK_TRANSACTION),
                    contributesToBalance,
                    isSuccessful,
                    MOCK_TRANSACTION.category,
                    MOCK_CARD_ITEMS
                )
            )
        }
    }

    @Test
    fun `Presenter error when loading data leads to error state`() = runBlockingTest {
        whenever(detailPresenter.flowTransactionById(any())).thenReturn(flowOf(MOCK_TRANSACTION))
        whenever(detailPresenter.isSuccessful(any())).thenThrow()

        val vm = TransactionDetailViewModel(MOCK_TRANSACTION_ID, detailPresenter, cardsPresenter)

        vm.observeStateAndEvents { stateObserver, _ ->
            launch {
                vm.collectTransaction()
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

        val vm = TransactionDetailViewModel(MOCK_TRANSACTION_ID, detailPresenter, cardsPresenter)

        vm.observeStateAndEvents { _, eventsObserver ->
            vm.onNoteAction()

            val navDirections = toTextInput(
                TextInputType.NOTE,
                NOTE_INPUT_REQUEST_KEY,
                initialValue = currentNote
            )

            eventsObserver.assertObserved(
                NavigateToNoteInputEvent(navDirections)
            )
        }
    }

    @Test
    fun `Clicking category select card action produces navigate to category select event`() = runBlockingTest {
        val currentCategory = TransactionCategory.ENTERTAINMENT

        whenever(detailPresenter.getCategory(any())).thenReturn(currentCategory)

        val vm = TransactionDetailViewModel(MOCK_TRANSACTION_ID, detailPresenter, cardsPresenter)

        vm.observeStateAndEvents { _, eventsObserver ->
            vm.onCategoryAction()

            val navDirections = toCategorySelect(
                1,
                currentCategory
            )

            eventsObserver.assertObserved(
                NavigateToCategorySelectEvent(navDirections)
            )
        }
    }

    @Test
    fun `Clicking card description card action produces navigate to card detail event`() = runBlockingTest {
        val vm = TransactionDetailViewModel(MOCK_TRANSACTION_ID, detailPresenter, cardsPresenter)

        vm.observeStateAndEvents { _, eventsObserver ->
            vm.onCardAction()

            eventsObserver.assertObserved(
                NavigateToCardDetailEvent
            )
        }
    }

    @Test
    fun `Clicking download statement card action produces download statement event`() = runBlockingTest {
        val vm = TransactionDetailViewModel(MOCK_TRANSACTION_ID, detailPresenter, cardsPresenter)

        vm.observeStateAndEvents { _, eventsObserver ->
            vm.onStatementAction()

            eventsObserver.assertObserved(
                DownloadStatementEvent
            )
        }
    }

}
