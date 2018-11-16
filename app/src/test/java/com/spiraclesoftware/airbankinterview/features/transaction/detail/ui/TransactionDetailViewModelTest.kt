package com.spiraclesoftware.airbankinterview.features.transaction.detail.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockito_kotlin.*
import com.spiraclesoftware.airbankinterview.features.transaction.detail.data.TransactionDetailRepository
import com.spiraclesoftware.airbankinterview.shared.domain.TransactionDetail
import com.spiraclesoftware.airbankinterview.features.transaction.list.data.TransactionListRepository
import com.spiraclesoftware.airbankinterview.shared.domain.Transaction
import com.spiraclesoftware.core.data.Resource
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class TransactionDetailViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var transactionDetailRepository: TransactionDetailRepository

    @Mock
    private lateinit var transactionListRepository: TransactionListRepository

    private lateinit var transactionDetailViewModel: TransactionDetailViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        transactionDetailViewModel = TransactionDetailViewModel(transactionDetailRepository, transactionListRepository)
    }

    @Test
    fun testNull() {
        assertThat(transactionDetailViewModel.transactionDetail, notNullValue())
        assertThat(transactionDetailViewModel.transaction, notNullValue())
        verify(transactionDetailRepository, never()).loadTransactionDetail(any())
        verify(transactionListRepository, never()).loadTransaction(any())
    }

    @Test
    fun dontFetchWithoutObservers() {
        transactionDetailViewModel.setTransactionId(1)
        verify(transactionDetailRepository, never()).loadTransactionDetail(any())
        verify(transactionListRepository, never()).loadTransaction(any())
    }

    @Test
    fun fetchWhenObserved() {
        transactionDetailViewModel.setTransactionId(1)
        transactionDetailViewModel.transactionDetail.observeForever(mock())
        transactionDetailViewModel.transaction.observeForever(mock())
        verify(transactionDetailRepository).loadTransactionDetail(any())
        verify(transactionListRepository).loadTransaction(any())
    }

    @Test
    fun transaction() {
        val observer = mock<Observer<Resource<Transaction>>>()
        transactionDetailViewModel.transaction.observeForever(observer)

        verifyNoMoreInteractions(observer)
        verifyNoMoreInteractions(transactionListRepository)

        transactionDetailViewModel.setTransactionId(1)
        verify(transactionListRepository).loadTransaction(any())
    }

    @Test
    fun transactionDetail() {
        val observer = mock<Observer<Resource<TransactionDetail>>>()
        transactionDetailViewModel.transactionDetail.observeForever(observer)

        verifyNoMoreInteractions(observer)
        verifyNoMoreInteractions(transactionDetailRepository)

        transactionDetailViewModel.setTransactionId(1)
        verify(transactionDetailRepository).loadTransactionDetail(any())
    }

    @Test
    fun retry() {
        transactionDetailViewModel.setTransactionId(1)
        verifyNoMoreInteractions(transactionDetailRepository)
        verifyNoMoreInteractions(transactionListRepository)

        transactionDetailViewModel.transactionDetail.observeForever(mock())
        verify(transactionDetailRepository).loadTransactionDetail(any())

        transactionDetailViewModel.transaction.observeForever(mock())
        verify(transactionListRepository).loadTransaction(any())

        reset(transactionDetailRepository)
        reset(transactionListRepository)
        transactionDetailViewModel.retry()
        verify(transactionDetailRepository).loadTransactionDetail(any())
        verify(transactionListRepository).loadTransaction(any())
    }
}