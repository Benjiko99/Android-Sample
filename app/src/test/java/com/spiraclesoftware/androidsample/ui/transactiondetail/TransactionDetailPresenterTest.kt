package com.spiraclesoftware.androidsample.ui.transactiondetail

import co.zsmb.rainbowcake.test.base.PresenterTest
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import com.spiraclesoftware.androidsample.TestData
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import com.spiraclesoftware.androidsample.ui.transactiondetail.cards.CardsGenerator
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionDetailPresenterTest : PresenterTest() {

    companion object {
        private val MOCK_TRANSACTION = TestData.transactions[0]

        private val MOCK_TRANSACTION_ID = MOCK_TRANSACTION.id

        private val MOCK_CARDS = CardsGenerator().makeCardsFor(MOCK_TRANSACTION)
    }

    @Mock
    private lateinit var transactionsInteractor: TransactionsInteractor

    @Mock
    private lateinit var cardsGenerator: CardsGenerator

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `Transaction is returned from interactor by ID`() = runBlockingTest {
        whenever(transactionsInteractor.getTransactionById(MOCK_TRANSACTION_ID)) doReturn MOCK_TRANSACTION

        val presenter = TransactionDetailPresenter(transactionsInteractor, cardsGenerator)

        val transaction = presenter.getTransactionById(MOCK_TRANSACTION_ID)
        assertEquals(MOCK_TRANSACTION, transaction)
    }

    @Test
    fun `Transaction is transformed to card items correctly`() = runBlockingTest {
        whenever(transactionsInteractor.getTransactionById(MOCK_TRANSACTION_ID)) doReturn MOCK_TRANSACTION
        whenever(cardsGenerator.makeCardsFor(MOCK_TRANSACTION)) doReturn MOCK_CARDS

        val presenter = TransactionDetailPresenter(transactionsInteractor, cardsGenerator)

        val transaction = presenter.getTransactionById(MOCK_TRANSACTION_ID)
        assertEquals(MOCK_TRANSACTION, transaction)
    }

}
