package com.spiraclesoftware.androidsample.features.transaction.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockito_kotlin.*
import com.spiraclesoftware.androidsample.shared.data.TransactionsRepository
import com.spiraclesoftware.androidsample.shared.domain.Transaction
import com.spiraclesoftware.androidsample.shared.domain.TransactionId
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
    private lateinit var transactionsRepository: TransactionsRepository

    private lateinit var transactionDetailViewModel: TransactionDetailViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        transactionDetailViewModel = TransactionDetailViewModel(transactionsRepository)
    }

    @Test
    fun testNull() {
        assertThat(transactionDetailViewModel.transaction, notNullValue())
        verify(transactionsRepository, never()).loadTransaction(any())
    }

    @Test
    fun dontFetchWithoutObservers() {
        transactionDetailViewModel.setTransactionId(TransactionId(1))
        verify(transactionsRepository, never()).loadTransaction(any())
    }

    @Test
    fun fetchWhenObserved() {
        transactionDetailViewModel.setTransactionId(TransactionId(1))
        transactionDetailViewModel.transaction.observeForever(mock())
        verify(transactionsRepository).loadTransaction(any())
    }

    @Test
    fun transaction() {
        val observer = mock<Observer<Resource<Transaction>>>()
        transactionDetailViewModel.transaction.observeForever(observer)

        verifyNoMoreInteractions(observer)
        verifyNoMoreInteractions(transactionsRepository)

        transactionDetailViewModel.setTransactionId(TransactionId(1))
        verify(transactionsRepository).loadTransaction(any())
    }
}