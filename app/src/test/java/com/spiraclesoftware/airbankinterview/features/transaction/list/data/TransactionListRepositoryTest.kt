package com.spiraclesoftware.airbankinterview.features.transaction.list.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.stub
import com.spiraclesoftware.airbankinterview.TestData
import com.spiraclesoftware.airbankinterview.application.api.ApiService
import com.spiraclesoftware.airbankinterview.shared.domain.Transaction
import com.spiraclesoftware.airbankinterview.utils.LiveDataTestUtil
import com.spiraclesoftware.core.data.InstantAppExecutors
import com.spiraclesoftware.core.data.Status
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class TransactionListRepositoryTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var transactionListCache: TransactionListCache

    private lateinit var transactionListRepository: TransactionListRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        transactionListRepository = TransactionListRepository(InstantAppExecutors(), apiService, transactionListCache)
    }

    private fun stubTransactionListCache(list: List<Transaction>? = null, single: Transaction? = null) {
        transactionListCache.stub {
            on { get() }.doReturn(list)
            on { get(any()) }.doReturn(single)
        }
    }

    @Test
    fun `Given that list is cached, return cached list`() {
        stubTransactionListCache(list = TestData.transactions)

        val transactionsLiveData = transactionListRepository.loadTransactionList()

        val transactionsResource = LiveDataTestUtil.getValue(transactionsLiveData)
        assertEquals(Status.SUCCESS, transactionsResource?.status)
        assertEquals(TestData.transactions, transactionsResource?.data)
    }

    @Test
    fun `Test filtering incoming transactions`() {
        stubTransactionListCache(list = TestData.transactions)

        val filter = TransactionListFilter(TransactionDirectionFilter.INCOMING_ONLY)

        val transactionsLiveData = transactionListRepository.loadTransactionList(filter)

        val transactionsResource = LiveDataTestUtil.getValue(transactionsLiveData)
        assertEquals(Status.SUCCESS, transactionsResource?.status)
        assertEquals(TestData.transactionsIncoming, transactionsResource?.data)
    }

    @Test
    fun `Test filtering outgoing transactions`() {
        stubTransactionListCache(list = TestData.transactions)

        val filter = TransactionListFilter(TransactionDirectionFilter.OUTGOING_ONLY)

        val transactionsLiveData = transactionListRepository.loadTransactionList(filter)

        val transactionsResource = LiveDataTestUtil.getValue(transactionsLiveData)
        assertEquals(Status.SUCCESS, transactionsResource?.status)
        assertEquals(TestData.transactionsOutgoing, transactionsResource?.data)
    }
}