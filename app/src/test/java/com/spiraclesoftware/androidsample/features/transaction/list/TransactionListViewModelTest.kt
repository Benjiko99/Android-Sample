package com.spiraclesoftware.androidsample.features.transaction.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockito_kotlin.*
import com.spiraclesoftware.androidsample.features.transaction.list.TransactionListViewModel
import com.spiraclesoftware.androidsample.shared.domain.TransactionDirectionFilter
import com.spiraclesoftware.androidsample.shared.data.TransactionsRepository
import com.spiraclesoftware.androidsample.shared.domain.Transaction
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
    private lateinit var transactionsRepository: TransactionsRepository

    private lateinit var transactionListViewModel: TransactionListViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        transactionListViewModel = TransactionListViewModel(transactionsRepository)
    }

    @Test
    fun testNull() {
        assertThat(transactionListViewModel.transactions, notNullValue())
        assertThat(transactionListViewModel.transactionListFilter, notNullValue())
        verify(transactionsRepository, never()).loadTransactionList(any())
    }

    @Test
    fun dontFetchWithoutObservers() {
        transactionListViewModel.setTransactionDirectionFilter(TransactionDirectionFilter.ALL)
        verify(transactionsRepository, never()).loadTransactionList(any())
    }

    @Test
    fun fetchWhenObserved() {
        transactionListViewModel.setTransactionDirectionFilter(TransactionDirectionFilter.ALL)
        transactionListViewModel.transactions.observeForever(mock())
        verify(transactionsRepository).loadTransactionList(any())
    }

    @Test
    fun transactions() {
        val observer = mock<Observer<Resource<List<Transaction>>>>()
        transactionListViewModel.transactions.observeForever(observer)

        verifyNoMoreInteractions(observer)
        verifyNoMoreInteractions(transactionsRepository)

        transactionListViewModel.setTransactionDirectionFilter(TransactionDirectionFilter.ALL)
        verify(transactionsRepository).loadTransactionList(any())
    }

    @Test
    fun retry() {
        transactionListViewModel.setTransactionDirectionFilter(TransactionDirectionFilter.ALL)
        verifyNoMoreInteractions(transactionsRepository)

        transactionListViewModel.transactions.observeForever(mock())
        verify(transactionsRepository).loadTransactionList(any())

        reset(transactionsRepository)
        transactionListViewModel.retry()
        verify(transactionsRepository).loadTransactionList(any())
    }
}