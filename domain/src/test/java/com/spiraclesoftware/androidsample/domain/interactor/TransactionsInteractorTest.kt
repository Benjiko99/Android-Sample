package com.spiraclesoftware.androidsample.domain.interactor

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.spiraclesoftware.androidsample.domain.*
import com.spiraclesoftware.androidsample.domain.model.*
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

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
    private lateinit var remoteDataSource: RemoteDataSource

    @Mock
    private lateinit var localDataSource: LocalDataSource

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `Transactions flow contains correct data`() = runBlockingTest {
        whenever(localDataSource.flowTransactions()) doReturn flowOf(MOCK_TRANSACTIONS)
        whenever(remoteDataSource.fetchTransactions()) doReturn flowOf(Result.Success(MOCK_TRANSACTIONS))

        val interactor = TransactionsInteractor(remoteDataSource, localDataSource)

        interactor.refreshTransactions()
        val transactions = interactor.flowTransactions()
        assertEquals(MOCK_TRANSACTIONS, transactions.single())
    }

    @Test
    fun `Transactions are correctly fetched from remote`() = runBlockingTest {
        whenever(remoteDataSource.fetchTransactions()) doReturn flowOf(Result.Success(MOCK_TRANSACTIONS))

        val interactor = TransactionsInteractor(remoteDataSource, localDataSource)

        val transactions = interactor.refreshTransactions()
        assertEquals(MOCK_TRANSACTIONS, transactions)

        verify(remoteDataSource).fetchTransactions()
    }

    @Test
    fun `Transactions are saved on local when fetched from remote`() = runBlockingTest {
        whenever(remoteDataSource.fetchTransactions()) doReturn flowOf(Result.Success(MOCK_TRANSACTIONS))

        val interactor = TransactionsInteractor(remoteDataSource, localDataSource)

        interactor.refreshTransactions()

        verify(localDataSource).saveTransactions(MOCK_TRANSACTIONS)
    }

    @Test
    fun `Transaction is retrieved correctly from local by ID`() = runBlockingTest {
        whenever(localDataSource.getTransactionById(any())) doReturn MOCK_TRANSACTION

        val interactor = TransactionsInteractor(remoteDataSource, localDataSource)

        val transaction = interactor.getTransactionById(MOCK_TRANSACTION.id)
        assertEquals(MOCK_TRANSACTION, transaction)
    }

}
