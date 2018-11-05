package com.spiraclesoftware.airbankinterview.transaction.detail.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.stub
import com.spiraclesoftware.airbankinterview.TestData
import com.spiraclesoftware.airbankinterview.api.ApiService
import com.spiraclesoftware.airbankinterview.transaction.detail.domain.TransactionDetail
import com.spiraclesoftware.airbankinterview.utils.LiveDataTestUtil
import com.spiraclesoftware.core.data.InstantAppExecutors
import com.spiraclesoftware.core.data.Status
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class TransactionDetailRepositoryTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var transactionDetailCache: TransactionDetailCache

    private lateinit var transactionDetailRepository: TransactionDetailRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        transactionDetailRepository =
                TransactionDetailRepository(InstantAppExecutors(), apiService, transactionDetailCache)
    }

    private fun stubTransactionDetailCache(single: TransactionDetail? = null) {
        transactionDetailCache.stub {
            on { get(any()) }.doReturn(single)
        }
    }

    @Test
    fun `Given that detail is cached, return cached detail`() {
        stubTransactionDetailCache(single = TestData.transactionDetail)

        val transactionDetailLiveData = transactionDetailRepository.loadTransactionDetail(1)

        val transactionDetailResource = LiveDataTestUtil.getValue(transactionDetailLiveData)
        assertEquals(Status.SUCCESS, transactionDetailResource?.status)
        assertEquals(TestData.transactionDetail, transactionDetailResource?.data)
    }
}