package com.spiraclesoftware.androidsample.shared.domain

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.spiraclesoftware.androidsample.TestData
import com.spiraclesoftware.androidsample.shared.data.disk.DiskDataSource
import com.spiraclesoftware.androidsample.shared.data.network.NetworkDataSource
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionsInteractorTest {

    companion object {
        private val MOCK_TRANSACTION = TestData.transactions[0]
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
        whenever(diskDataSource.getTransactions()) doReturn TestData.transactions
        whenever(networkDataSource.getTransactions()) doReturn emptyList()

        val interactor = TransactionsInteractor(networkDataSource, diskDataSource)

        assertEquals(TestData.transactions, interactor.getTransactions())
    }

    @Test
    fun `Transactions are loaded correctly from network`() = runBlockingTest {
        whenever(diskDataSource.getTransactions()) doReturn null
        whenever(networkDataSource.getTransactions()) doReturn TestData.transactions

        val interactor = TransactionsInteractor(networkDataSource, diskDataSource)

        assertEquals(TestData.transactions, interactor.getTransactions())
    }

    @Test
    fun `Transactions are saved on disk when loaded from network`() = runBlockingTest {
        whenever(diskDataSource.getTransactions()) doReturn null
        whenever(networkDataSource.getTransactions()) doReturn TestData.transactions

        val interactor = TransactionsInteractor(networkDataSource, diskDataSource)

        interactor.getTransactions()

        verify(diskDataSource).saveTransactions(TestData.transactions)
    }

    @Test
    fun `Transaction is loaded correctly from disk by ID`() = runBlockingTest {
        whenever(diskDataSource.getTransactionById(MOCK_TRANSACTION_ID)) doReturn MOCK_TRANSACTION
        whenever(networkDataSource.getTransactions()) doReturn emptyList()

        val interactor = TransactionsInteractor(networkDataSource, diskDataSource)

        assertEquals(MOCK_TRANSACTION, interactor.getTransactionById(MOCK_TRANSACTION_ID))
    }

    @Test
    fun `Transaction is loaded correctly from network by ID`() = runBlockingTest {
        whenever(diskDataSource.getTransactionById(MOCK_TRANSACTION_ID)) doReturn null
        whenever(networkDataSource.getTransactions()) doReturn TestData.transactions

        val interactor = TransactionsInteractor(networkDataSource, diskDataSource)

        assertEquals(MOCK_TRANSACTION, interactor.getTransactionById(MOCK_TRANSACTION_ID))
    }

    @Test
    fun `Transaction is saved on disk when loaded from network by ID`() = runBlockingTest {
        whenever(diskDataSource.getTransactionById(MOCK_TRANSACTION_ID)) doReturn null
        whenever(networkDataSource.getTransactions()) doReturn TestData.transactions

        val interactor = TransactionsInteractor(networkDataSource, diskDataSource)

        interactor.getTransactionById(MOCK_TRANSACTION_ID)

        verify(diskDataSource).saveTransactions(TestData.transactions)
    }

}
