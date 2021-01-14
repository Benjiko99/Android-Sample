package com.spiraclesoftware.androidsample.transaction_detail

import co.zsmb.rainbowcake.test.base.PresenterTest
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import com.spiraclesoftware.androidsample.domain.model.*
import com.spiraclesoftware.androidsample.epochDateTime
import com.spiraclesoftware.androidsample.money
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
        private val MOCK_TRANSACTION = Transaction(
            TransactionId("1"),
            "Paypal *Steam",
            epochDateTime,
            money("49.99", "EUR"),
            TransferDirection.OUTGOING,
            TransactionCategory.ENTERTAINMENT,
            TransactionStatus.COMPLETED,
            TransactionStatusCode.SUCCESSFUL,
        )
    }

    @Mock
    private lateinit var transactionsInteractor: TransactionsInteractor

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `Transaction is returned from interactor by ID`() = runBlockingTest {
        whenever(transactionsInteractor.getTransactionById(MOCK_TRANSACTION.id)) doReturn MOCK_TRANSACTION

        val presenter = TransactionDetailPresenter(transactionsInteractor)

        val transaction = presenter.getTransactionById(MOCK_TRANSACTION.id)
        assertEquals(MOCK_TRANSACTION, transaction)
    }

}
