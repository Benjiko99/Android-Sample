package com.spiraclesoftware.androidsample.ui.transactiondetail

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.TestData
import com.spiraclesoftware.androidsample.ui.shared.MoneyFormat
import com.spiraclesoftware.androidsample.ui.transactiondetail.TransactionDetailViewModel.FeatureNotImplementedEvent
import com.spiraclesoftware.androidsample.ui.transactiondetail.cards.CardItem
import com.spiraclesoftware.androidsample.ui.transactiondetail.cards.CardsPresenter
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

        private val MOCK_CARD_ITEMS = emptyList<CardItem>()
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

        whenever(detailPresenter.getTransactionById(any())) doReturn MOCK_TRANSACTION
        whenever(cardsPresenter.getCardItems(any(), any())) doReturn MOCK_CARD_ITEMS
        whenever(detailPresenter.contributesToBalance(any())) doReturn contributesToBalance
        whenever(detailPresenter.isSuccessful(any())) doReturn isSuccessful

        val vm = TransactionDetailViewModel(detailPresenter, cardsPresenter)

        vm.observeStateAndEvents { stateObserver, eventsObserver ->
            vm.setTransactionId(MOCK_TRANSACTION_ID)
            vm.loadData()

            stateObserver.assertObserved(
                Loading,
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
        whenever(detailPresenter.getTransactionById(any())).thenAnswer {
            throw IOException()
        }

        val vm = TransactionDetailViewModel(detailPresenter, cardsPresenter)

        vm.observeStateAndEvents { stateObserver, eventsObserver ->
            vm.setTransactionId(MOCK_TRANSACTION_ID)
            vm.loadData()
            stateObserver.assertObserved(
                Loading,
                Error
            )
        }
    }

    @Test
    fun `Retrying after error loads correctly into ready state`() = runBlockingTest {
        val contributesToBalance = true
        val isSuccessful = true

        whenever(detailPresenter.getTransactionById(any())) doReturn MOCK_TRANSACTION
        whenever(cardsPresenter.getCardItems(any(), any())) doReturn MOCK_CARD_ITEMS
        whenever(detailPresenter.contributesToBalance(any())) doReturn contributesToBalance
        whenever(detailPresenter.isSuccessful(any())) doReturn isSuccessful

        var invocations = 0
        whenever(detailPresenter.getTransactionById(any())).thenAnswer {
            when (invocations++) {
                0 -> throw IOException()
                else -> MOCK_TRANSACTION
            }
        }

        val vm = TransactionDetailViewModel(detailPresenter, cardsPresenter)

        vm.observeStateAndEvents { stateObserver, eventsObserver ->
            vm.setTransactionId(MOCK_TRANSACTION_ID)
            vm.loadData()
            vm.retry()

            stateObserver.assertObserved(
                Loading,
                Error,
                Loading,
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
    fun `Clicking any card action produces feature not implemented event`() = runBlockingTest {
        val vm = TransactionDetailViewModel(detailPresenter, cardsPresenter)

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
