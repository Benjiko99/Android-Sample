package com.spiraclesoftware.androidsample.domain.interactor

import com.nhaarman.mockitokotlin2.*
import com.spiraclesoftware.androidsample.TestData
import com.spiraclesoftware.androidsample.data.disk.DiskDataSource
import com.spiraclesoftware.androidsample.data.network.NetworkDataSource
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.internal.verification.Only
import org.mockito.internal.verification.Times

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionsInteractorTest {

    companion object {
        private val MOCK_TRANSACTIONS = TestData.transactions
        private val MOCK_TRANSACTION = MOCK_TRANSACTIONS[0]
        private val MOCK_TRANSACTION_ID = MOCK_TRANSACTION.id
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
        whenever(diskDataSource.getTransactions()) doReturn MOCK_TRANSACTIONS
        whenever(networkDataSource.getTransactions()) doReturn emptyList()

        val interactor = TransactionsInteractor(
            networkDataSource,
            diskDataSource
        )

        val transactions = interactor.getTransactions()
        assertEquals(MOCK_TRANSACTIONS, transactions)

        verifyZeroInteractions(networkDataSource)
        verify(diskDataSource, Only()).getTransactions()
    }

    @Test
    fun `Transactions are loaded correctly from network`() = runBlockingTest {
        whenever(diskDataSource.getTransactions()) doReturn null
        whenever(networkDataSource.getTransactions()) doReturn MOCK_TRANSACTIONS

        val interactor = TransactionsInteractor(
            networkDataSource,
            diskDataSource
        )

        val transactions = interactor.getTransactions()
        assertEquals(MOCK_TRANSACTIONS, transactions)

        verify(diskDataSource).getTransactions()
        verify(networkDataSource).getTransactions()
    }

    @Test
    fun `Transactions are loaded from network when ignoring cached`() = runBlockingTest {
        whenever(diskDataSource.getTransactions()) doReturn null
        whenever(networkDataSource.getTransactions()) doReturn MOCK_TRANSACTIONS

        val interactor = TransactionsInteractor(
            networkDataSource,
            diskDataSource
        )
        interactor.getTransactions(ignoreCached = true)

        verify(networkDataSource).getTransactions()
        verify(diskDataSource, Times(0)).getTransactions()
    }

    @Test
    fun `Transactions are saved on disk when loaded from network`() = runBlockingTest {
        whenever(diskDataSource.getTransactions()) doReturn null
        whenever(networkDataSource.getTransactions()) doReturn MOCK_TRANSACTIONS

        val interactor = TransactionsInteractor(
            networkDataSource,
            diskDataSource
        )
        interactor.getTransactions()

        verify(diskDataSource).saveTransactions(MOCK_TRANSACTIONS)
    }

    @Test
    fun `Transaction is loaded correctly from disk by ID`() = runBlockingTest {
        whenever(diskDataSource.getTransactionById(any())) doReturn MOCK_TRANSACTION
        whenever(networkDataSource.getTransactions()) doReturn emptyList()

        val interactor = TransactionsInteractor(
            networkDataSource,
            diskDataSource
        )

        val transaction = interactor.getTransactionById(MOCK_TRANSACTION_ID)
        assertEquals(MOCK_TRANSACTION, transaction)

        verify(diskDataSource).getTransactionById(MOCK_TRANSACTION_ID)
        verify(networkDataSource, Times(0)).getTransactions()
    }

    @Test
    fun `Transaction is loaded correctly from network by ID`() = runBlockingTest {
        whenever(diskDataSource.getTransactionById(any())) doReturn null
        whenever(networkDataSource.getTransactions()) doReturn MOCK_TRANSACTIONS

        val interactor = TransactionsInteractor(
            networkDataSource,
            diskDataSource
        )

        val transaction = interactor.getTransactionById(MOCK_TRANSACTION_ID)
        assertEquals(MOCK_TRANSACTION, transaction)

        verify(networkDataSource).getTransactions()
        verify(diskDataSource, Times(0)).getTransactions()
    }

    @Test
    fun `Transaction is saved on disk when loaded from network by ID`() = runBlockingTest {
        whenever(diskDataSource.getTransactionById(any())) doReturn null
        whenever(networkDataSource.getTransactions()) doReturn MOCK_TRANSACTIONS

        val interactor = TransactionsInteractor(
            networkDataSource,
            diskDataSource
        )
        interactor.getTransactionById(MOCK_TRANSACTION_ID)

        verify(diskDataSource).saveTransactions(MOCK_TRANSACTIONS)
    }

}
