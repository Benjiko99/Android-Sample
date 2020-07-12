package com.spiraclesoftware.androidsample.shared.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.stub
import com.spiraclesoftware.androidsample.TestData
import com.spiraclesoftware.androidsample.application.data.ApiService
import com.spiraclesoftware.androidsample.shared.domain.Transaction
import com.spiraclesoftware.androidsample.shared.domain.TransactionId
import com.spiraclesoftware.androidsample.shared.domain.TransactionListFilter
import com.spiraclesoftware.androidsample.shared.domain.TransferDirectionFilter
import com.spiraclesoftware.androidsample.utils.LiveDataTestUtil
import com.spiraclesoftware.core.data.AssociatedListCache
import com.spiraclesoftware.core.data.InstantAppExecutors
import com.spiraclesoftware.core.data.Status
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class TransactionsRepositoryTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var listCache: AssociatedListCache<TransactionId, Transaction>

    private lateinit var transactionsRepository: TransactionsRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        transactionsRepository =
            TransactionsRepository(
                InstantAppExecutors(),
                apiService,
                listCache
            )
    }

    private fun stubTransactionListCache(
        list: List<Transaction>? = null,
        single: Transaction? = null
    ) {
        listCache.stub {
            on { get() }.doReturn(list)
            on { get(any()) }.doReturn(single)
        }
    }

    @Test
    fun `Given that list is cached, return cached list`() {
        stubTransactionListCache(list = TestData.transactions)

        val transactionsLiveData = transactionsRepository.loadTransactionList()

        val transactionsResource = LiveDataTestUtil.getValue(transactionsLiveData)
        assertEquals(Status.SUCCESS, transactionsResource?.status)
        assertEquals(TestData.transactions, transactionsResource?.data)
    }

    @Test
    fun `Test filtering incoming transactions`() {
        stubTransactionListCache(list = TestData.transactions)

        val filter = TransactionListFilter(TransferDirectionFilter.INCOMING_ONLY)

        val transactionsLiveData = transactionsRepository.loadTransactionList(filter)

        val transactionsResource = LiveDataTestUtil.getValue(transactionsLiveData)
        assertEquals(Status.SUCCESS, transactionsResource?.status)
        assertEquals(TestData.transactionsIncoming, transactionsResource?.data)
    }

    @Test
    fun `Test filtering outgoing transactions`() {
        stubTransactionListCache(list = TestData.transactions)

        val filter = TransactionListFilter(TransferDirectionFilter.OUTGOING_ONLY)

        val transactionsLiveData = transactionsRepository.loadTransactionList(filter)

        val transactionsResource = LiveDataTestUtil.getValue(transactionsLiveData)
        assertEquals(Status.SUCCESS, transactionsResource?.status)
        assertEquals(TestData.transactionsOutgoing, transactionsResource?.data)
    }
}