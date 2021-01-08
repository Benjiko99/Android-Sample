package com.spiraclesoftware.androidsample.domain.interactor

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.spiraclesoftware.androidsample.domain.DiskDataSource
import com.spiraclesoftware.androidsample.domain.NetworkDataSource
import com.spiraclesoftware.androidsample.domain.epochDateTime
import com.spiraclesoftware.androidsample.domain.model.*
import com.spiraclesoftware.androidsample.domain.money
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.internal.verification.Times

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionsInteractorTest {

    companion object {
        private val MOCK_TRANSACTIONS = listOf(
            Transaction(
                TransactionId("1"),
                "Paypal *Steam",
                epochDateTime,
                money("49.99", "EUR"),
                TransferDirection.OUTGOING,
                TransactionCategory.ENTERTAINMENT,
                TransactionStatus.COMPLETED,
                TransactionStatusCode.SUCCESSFUL,
            ),
            Transaction(
                TransactionId("2"),
                "Salary",
                epochDateTime,
                money("1000", "EUR"),
                TransferDirection.INCOMING,
                TransactionCategory.TRANSFERS,
                TransactionStatus.COMPLETED,
                TransactionStatusCode.SUCCESSFUL
            ),
        )

        private val MOCK_TRANSACTION = MOCK_TRANSACTIONS[0]
    }

    @Mock
    private lateinit var networkDataSource: NetworkDataSource

    @Mock
    private lateinit var diskDataSource: DiskDataSource

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `Transactions are loaded correctly from disk`() = runBlockingTest {
        whenever(diskDataSource.flowTransactions()) doReturn flowOf(MOCK_TRANSACTIONS)

        val interactor = TransactionsInteractor(networkDataSource, diskDataSource)

        val transactions = interactor.flowTransactions()
        assertEquals(MOCK_TRANSACTIONS, transactions.single())
    }

    @Test
    fun `Transactions are correctly fetched from network`() = runBlockingTest {
        whenever(networkDataSource.fetchTransactions()) doReturn MOCK_TRANSACTIONS

        val interactor = TransactionsInteractor(networkDataSource, diskDataSource)

        val transactions = interactor.fetchTransactions()
        assertEquals(MOCK_TRANSACTIONS, transactions)

        verify(networkDataSource).fetchTransactions()
    }

    @Test
    fun `Transactions are saved on disk when fetched from network`() = runBlockingTest {
        whenever(networkDataSource.fetchTransactions()) doReturn MOCK_TRANSACTIONS

        val interactor = TransactionsInteractor(networkDataSource, diskDataSource)

        interactor.fetchTransactions()

        verify(diskDataSource).saveTransactions(MOCK_TRANSACTIONS)
    }

    @Test
    fun `Transaction is loaded correctly from disk by ID`() = runBlockingTest {
        whenever(diskDataSource.getTransactionById(any())) doReturn MOCK_TRANSACTION
        whenever(networkDataSource.fetchTransactions()) doReturn emptyList()

        val interactor = TransactionsInteractor(networkDataSource, diskDataSource)

        val transaction = interactor.getTransactionById(MOCK_TRANSACTION.id)
        assertEquals(MOCK_TRANSACTION, transaction)

        verify(diskDataSource).getTransactionById(MOCK_TRANSACTION.id)
        verify(networkDataSource, Times(0)).fetchTransactions()
    }

    @Test
    fun `Transaction is loaded correctly from network by ID`() = runBlockingTest {
        whenever(diskDataSource.getTransactionById(any())) doReturn null
        whenever(networkDataSource.fetchTransactions()) doReturn MOCK_TRANSACTIONS

        val interactor = TransactionsInteractor(networkDataSource, diskDataSource)

        val transaction = interactor.getTransactionById(MOCK_TRANSACTION.id)
        assertEquals(MOCK_TRANSACTION, transaction)

        verify(networkDataSource).fetchTransactions()
    }

    @Test
    fun `Transaction is saved on disk when loaded from network by ID`() = runBlockingTest {
        whenever(diskDataSource.getTransactionById(any())) doReturn null
        whenever(networkDataSource.fetchTransactions()) doReturn MOCK_TRANSACTIONS

        val interactor = TransactionsInteractor(networkDataSource, diskDataSource)
        interactor.getTransactionById(MOCK_TRANSACTION.id)

        verify(diskDataSource).saveTransactions(MOCK_TRANSACTIONS)
    }

}
