package com.spiraclesoftware.androidsample.domain.interactor

import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.domain.LocalDataSource
import com.spiraclesoftware.androidsample.domain.RemoteDataSource
import com.spiraclesoftware.androidsample.domain.core.Result
import com.spiraclesoftware.androidsample.domain.core.data
import com.spiraclesoftware.androidsample.domain.entity.Transaction
import com.spiraclesoftware.androidsample.domain.entity.TransactionsFilter
import com.spiraclesoftware.androidsample.domain.entity.TransferDirection
import com.spiraclesoftware.androidsample.domain.entity.TransferDirectionFilter
import com.spiraclesoftware.androidsample.domain.transaction
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionsInteractorTest {

    @MockK
    lateinit var remoteDataSource: RemoteDataSource

    @MockK
    lateinit var localDataSource: LocalDataSource

    @InjectMockKs
    lateinit var testSubject: TransactionsInteractor

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun flowTransactions_all() = runBlockingTest {
        val transactions = listOf<Transaction>(mockk())

        every { localDataSource.flowTransactions() } returns flowOf(transactions)
        coEvery { remoteDataSource.fetchTransactions() } returns flowOf(Result.Success(transactions))
        coJustRun { localDataSource.saveTransactions(any()) }

        testSubject.refreshTransactions()
        val actual = testSubject.flowTransactions(flowOf(TransactionsFilter()))

        assertThat(actual.first().data).isEqualTo(transactions)
    }

    @Test
    fun flowTransactions_filtered() = runBlockingTest {
        val transactions = listOf(
            transaction(transferDirection = TransferDirection.INCOMING),
            transaction(transferDirection = TransferDirection.OUTGOING),
        )

        every { localDataSource.flowTransactions() } returns flowOf(transactions)
        coEvery { remoteDataSource.fetchTransactions() } returns flowOf(Result.Success(transactions))
        coJustRun { localDataSource.saveTransactions(any()) }

        val filter = TransactionsFilter(directionFilter = TransferDirectionFilter.INCOMING_ONLY)

        testSubject.refreshTransactions()
        val actual = testSubject.flowTransactions(flowOf(filter))

        assertThat(actual.first().data).isEqualTo(transactions.take(1))
    }

    @Test
    fun whenRefreshTransactions_thenSaveTransactionsLocally() = runBlockingTest {
        val transactions = listOf<Transaction>(mockk())

        coEvery { remoteDataSource.fetchTransactions() } returns flowOf(Result.Success(transactions))
        coJustRun { localDataSource.saveTransactions(any()) }

        testSubject.refreshTransactions()

        verify { localDataSource.saveTransactions(transactions) }
    }

    @Test
    fun retrieveTransactionById() = runBlockingTest {
        val transaction = transaction()
        every { localDataSource.getTransactionById(any()) } returns transaction

        val actual = testSubject.getTransactionById(transaction.id)
        assertThat(actual).isEqualTo(transaction)
    }

}
