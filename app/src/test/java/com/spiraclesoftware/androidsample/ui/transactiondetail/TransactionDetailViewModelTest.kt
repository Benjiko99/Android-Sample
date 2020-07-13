package com.spiraclesoftware.androidsample.ui.transactiondetail

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.TestData
import com.spiraclesoftware.androidsample.ui.transactiondetail.TransactionDetailViewModel.FeatureNotImplementedEvent
import com.spiraclesoftware.androidsample.ui.transactiondetail.TransactionDetailViewModel.LoadFailedEvent
import com.spiraclesoftware.androidsample.ui.transactiondetail.cards.CardsGenerator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionDetailViewModelTest : ViewModelTest() {

    companion object {
        private val MOCK_TRANSACTION = TestData.transactions[0]

        private val MOCK_TRANSACTION_ID = MOCK_TRANSACTION.id

        private val MOCK_CARDS = CardsGenerator().makeCardsFor(MOCK_TRANSACTION)
    }

    @Mock
    private lateinit var detailPresenter: TransactionDetailPresenter

    @Mock
    private lateinit var cardsGenerator: CardsGenerator

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `Transaction is loaded correctly from presenter by ID`() = runBlockingTest {
        whenever(detailPresenter.getTransactionById(MOCK_TRANSACTION_ID)) doReturn MOCK_TRANSACTION
        whenever(cardsGenerator.makeCardsFor(MOCK_TRANSACTION)) doReturn MOCK_CARDS

        val vm = TransactionDetailViewModel(detailPresenter, cardsGenerator)

        vm.observeStateAndEvents { stateObserver, eventsObserver ->
            vm.loadTransaction(MOCK_TRANSACTION_ID)

            stateObserver.assertObserved(
                Loading,
                DetailReady(
                    MOCK_TRANSACTION,
                    MOCK_CARDS
                )
            )
        }
    }

    @Test
    fun `Transaction loading exception produces error event`() = runBlockingTest {
        whenever(detailPresenter.getTransactionById(any())).thenAnswer {
            throw IOException()
        }

        val vm = TransactionDetailViewModel(detailPresenter, cardsGenerator)

        vm.observeStateAndEvents { stateObserver, eventsObserver ->
            vm.loadTransaction(MOCK_TRANSACTION_ID)

            eventsObserver.assertObserved(LoadFailedEvent)
        }
    }

    @Test
    fun `Clicking any card action produces feature not implemented event`() = runBlockingTest {
        val vm = TransactionDetailViewModel(detailPresenter, cardsGenerator)

        vm.observeStateAndEvents { stateObserver, eventsObserver ->
            vm.onCardActionClicked(R.id.card_action__card_detail)
            vm.onCardActionClicked(R.id.card_action__download_statement)
            vm.onCardActionClicked(R.id.card_action__change_category)
            vm.onCardActionClicked(R.id.card_action__change_note)

            eventsObserver.assertObserved(
                FeatureNotImplementedEvent,
                FeatureNotImplementedEvent,
                FeatureNotImplementedEvent,
                FeatureNotImplementedEvent
            )
        }
    }

}
