package com.spiraclesoftware.androidsample.features.transaction.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockito_kotlin.*
import com.spiraclesoftware.androidsample.shared.data.TransactionsRepository
import com.spiraclesoftware.androidsample.shared.domain.Transaction
import com.spiraclesoftware.androidsample.shared.domain.TransactionId
import com.spiraclesoftware.androidsample.shared.domain.TransferDirectionFilter
import com.spiraclesoftware.androidsample.utils.LiveDataTestUtil
import com.spiraclesoftware.core.data.Resource
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

    @Mock
    private lateinit var transactionsRepository: TransactionsRepository

    private lateinit var transactionListViewModel: TransactionListViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        transactionListViewModel = TransactionListViewModel(transactionsRepository)
    }

    @Test
    fun testNull() {
        assertNotNull(transactionListViewModel.transactions)
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
        transactionListViewModel.transactions.observeForever(mock())
        verify(transactionsRepository).loadTransactionList(any())
    }

    @Test
    fun transactions() {
        val observer = mock<Observer<Resource<List<Transaction>>>>()
        transactionListViewModel.transactions.observeForever(observer)

        verifyNoMoreInteractions(observer)
        verifyNoMoreInteractions(transactionsRepository)

        transactionListViewModel.setTransferDirectionFilter(TransferDirectionFilter.ALL)
        verify(transactionsRepository).loadTransactionList(any())
    }

    @Test
    fun retry() {
        transactionListViewModel.setTransferDirectionFilter(TransferDirectionFilter.ALL)
        verifyNoMoreInteractions(transactionsRepository)

        transactionListViewModel.transactions.observeForever(mock())
        verify(transactionsRepository).loadTransactionList(any())

        reset(transactionsRepository)
        transactionListViewModel.retry()
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