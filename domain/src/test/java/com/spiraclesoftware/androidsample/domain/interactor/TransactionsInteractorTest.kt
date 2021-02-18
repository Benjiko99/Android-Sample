package com.spiraclesoftware.androidsample.domain.interactor

import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.domain.*
import com.spiraclesoftware.androidsample.domain.entity.*
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

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

    @MockK
    lateinit var remoteDataSource: RemoteDataSource

    @MockK
    lateinit var localDataSource: LocalDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `Transactions flow contains correct data`() = runBlockingTest {
        every { localDataSource.flowTransactions() } returns flowOf(MOCK_TRANSACTIONS)
        coEvery { remoteDataSource.fetchTransactions() } returns flowOf(Result.Success(MOCK_TRANSACTIONS))
        coJustRun { localDataSource.saveTransactions(any()) }

        val interactor = TransactionsInteractor(remoteDataSource, localDataSource)

        interactor.refreshTransactions()
        val transactions = interactor.flowTransactions()

        assertThat(transactions.first().data).isEqualTo(MOCK_TRANSACTIONS)
    }

    @Test
    fun `Transactions are saved on local when fetched from remote`() = runBlockingTest {
        coEvery { remoteDataSource.fetchTransactions() } returns flowOf(Result.Success(MOCK_TRANSACTIONS))
        coJustRun { localDataSource.saveTransactions(any()) }

        val interactor = TransactionsInteractor(remoteDataSource, localDataSource)

        interactor.refreshTransactions()

        verify { localDataSource.saveTransactions(MOCK_TRANSACTIONS) }
    }

    @Test
    fun `Transaction is retrieved correctly from local by ID`() = runBlockingTest {
        every { localDataSource.getTransactionById(any()) } returns MOCK_TRANSACTION

        val interactor = TransactionsInteractor(remoteDataSource, localDataSource)

        val transaction = interactor.getTransactionById(MOCK_TRANSACTION.id)
        assertThat(transaction).isEqualTo(MOCK_TRANSACTION)
    }

}
