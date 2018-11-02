package com.spiraclesoftware.airbankinterview.transaction.list.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockito_kotlin.*
import com.spiraclesoftware.airbankinterview.transaction.list.data.TransactionDirectionFilter
import com.spiraclesoftware.airbankinterview.transaction.list.data.TransactionListRepository
import com.spiraclesoftware.airbankinterview.transaction.list.domain.Transaction
import com.spiraclesoftware.core.data.Resource
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Assert.assertThat
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
    private lateinit var transactionListRepository: TransactionListRepository

    private lateinit var transactionListViewModel: TransactionListViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        transactionListViewModel = TransactionListViewModel(transactionListRepository)
    }

    @Test
    fun testNull() {
        assertThat(transactionListViewModel.transactions, notNullValue())
        assertThat(transactionListViewModel.transactionListFilter, notNullValue())
        verify(transactionListRepository, never()).loadTransactionList(any())
    }

    @Test
    fun dontFetchWithoutObservers() {
        transactionListViewModel.setTransactionDirectionFilter(TransactionDirectionFilter.ALL)
        verify(transactionListRepository, never()).loadTransactionList(any())
    }

    @Test
    fun fetchWhenObserved() {
        transactionListViewModel.setTransactionDirectionFilter(TransactionDirectionFilter.ALL)
        transactionListViewModel.transactions.observeForever(mock())
        verify(transactionListRepository).loadTransactionList(any())
    }

    @Test
    fun transactions() {
        val observer = mock<Observer<Resource<List<Transaction>>>>()
        transactionListViewModel.transactions.observeForever(observer)

        verifyNoMoreInteractions(observer)
        verifyNoMoreInteractions(transactionListRepository)

        transactionListViewModel.setTransactionDirectionFilter(TransactionDirectionFilter.ALL)
        verify(transactionListRepository).loadTransactionList(any())
    }

    @Test
    fun retry() {
        transactionListViewModel.setTransactionDirectionFilter(TransactionDirectionFilter.ALL)
        verifyNoMoreInteractions(transactionListRepository)

        transactionListViewModel.transactions.observeForever(mock())
        verify(transactionListRepository).loadTransactionList(any())

        reset(transactionListRepository)
        transactionListViewModel.retry()
        verify(transactionListRepository).loadTransactionList(any())
    }
}