package com.spiraclesoftware.androidsample.features.transaction.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.*
import com.spiraclesoftware.androidsample.shared.data.AccountRepository
import com.spiraclesoftware.androidsample.shared.data.ConversionRatesRepository
import com.spiraclesoftware.androidsample.shared.data.TransactionsRepository
import com.spiraclesoftware.androidsample.shared.domain.*
import com.spiraclesoftware.androidsample.utils.LiveDataTestUtil
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class TransactionListViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val accountRepository = AccountRepository()

    @Mock
    private lateinit var transactionsRepository: TransactionsRepository

    @Mock
    private lateinit var ratesRepository: ConversionRatesRepository

    private lateinit var transactionListViewModel: TransactionListViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        transactionListViewModel = TransactionListViewModel(accountRepository, transactionsRepository, ratesRepository)
    }

    @Test
    fun testNull() {
        assertNotNull(transactionListViewModel.listData)
        assertNotNull(transactionListViewModel.transactionListFilter)
        verify(transactionsRepository, never()).loadTransactionList(any())
    }

    @Test
    fun dontFetchWithoutObservers() {
        transactionListViewModel.setTransferDirectionFilter(TransferDirectionFilter.ALL)
        verify(transactionsRepository, never()).loadTransactionList(any())
    }

    @Test
    fun fetchWhenObserved() {
        transactionListViewModel.setTransferDirectionFilter(TransferDirectionFilter.ALL)
        transactionListViewModel.listData.observeForever(mock())
        verify(transactionsRepository).loadTransactionList(any())
    }

    @Test
    fun transactions() {
        transactionListViewModel.listData.observeForever(mock())

        verifyNoMoreInteractions(transactionsRepository)

        transactionListViewModel.setTransferDirectionFilter(TransferDirectionFilter.ALL)
        verify(transactionsRepository).loadTransactionList(any())
    }

    @Test
    fun refresh() {
        transactionListViewModel.setTransferDirectionFilter(TransferDirectionFilter.ALL)
        verifyNoMoreInteractions(transactionsRepository)
        verifyNoMoreInteractions(ratesRepository)

        transactionListViewModel.listData.observeForever(mock())
        verify(transactionsRepository).loadTransactionList(any())

        reset(transactionsRepository)
        transactionListViewModel.refresh()
        verify(transactionsRepository).loadTransactionList(any())
    }

    @Test
    fun navigateToTransactionDetail() {
        val transactionId = TransactionId(1)
        transactionListViewModel.openTransactionDetail(transactionId)

        val navigateEvent =
            LiveDataTestUtil.getValue(transactionListViewModel.navigateToDetailAction)
        assertEquals(transactionId, navigateEvent?.getContentIfNotHandled())
    }
}