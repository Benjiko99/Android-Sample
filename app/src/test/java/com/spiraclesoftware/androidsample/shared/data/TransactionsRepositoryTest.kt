package com.spiraclesoftware.androidsample.shared.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.stub
import com.spiraclesoftware.androidsample.TestData
import com.spiraclesoftware.androidsample.application.data.ApiService
import com.spiraclesoftware.androidsample.shared.domain.*
import com.spiraclesoftware.androidsample.utils.LiveDataTestUtil
import com.spiraclesoftware.core.data.AssociatedItemCache
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

    @Mock
    private lateinit var detailCache: AssociatedItemCache<TransactionId, TransactionDetail>

    private lateinit var transactionsRepository: TransactionsRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        transactionsRepository =
                TransactionsRepository(
                    InstantAppExecutors(),
                    apiService,
                    listCache,
                    detailCache
                )
    }

    private fun stubTransactionListCache(list: List<Transaction>? = null, single: Transaction? = null) {
        listCache.stub {
            on { get() }.doReturn(list)
            on { get(any()) }.doReturn(single)
        }
    }

    private fun stubTransactionDetailCache(single: TransactionDetail? = null) {
        detailCache.stub {
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

        val filter = TransactionListFilter(TransactionDirectionFilter.INCOMING_ONLY)

        val transactionsLiveData = transactionsRepository.loadTransactionList(filter)

        val transactionsResource = LiveDataTestUtil.getValue(transactionsLiveData)
        assertEquals(Status.SUCCESS, transactionsResource?.status)
        assertEquals(TestData.transactionsIncoming, transactionsResource?.data)
    }

    @Test
    fun `Test filtering outgoing transactions`() {
        stubTransactionListCache(list = TestData.transactions)

        val filter = TransactionListFilter(TransactionDirectionFilter.OUTGOING_ONLY)

        val transactionsLiveData = transactionsRepository.loadTransactionList(filter)

        val transactionsResource = LiveDataTestUtil.getValue(transactionsLiveData)
        assertEquals(Status.SUCCESS, transactionsResource?.status)
        assertEquals(TestData.transactionsOutgoing, transactionsResource?.data)
    }

    @Test
    fun `Given that detail is cached, return cached detail`() {
        stubTransactionDetailCache(single = TestData.transactionDetail)

        val transactionDetailLiveData = transactionsRepository.loadTransactionDetail(TransactionId(1))

        val transactionDetailResource = LiveDataTestUtil.getValue(transactionDetailLiveData)
        assertEquals(Status.SUCCESS, transactionDetailResource?.status)
        assertEquals(TestData.transactionDetail, transactionDetailResource?.data)
    }
}