package com.spiraclesoftware.androidsample.features.transaction.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.*
import com.spiraclesoftware.androidsample.shared.data.AccountRepository
import com.spiraclesoftware.androidsample.shared.data.ConversionRatesRepository
import com.spiraclesoftware.androidsample.shared.data.TransactionsRepository
import com.spiraclesoftware.androidsample.shared.domain.TransactionId
import com.spiraclesoftware.androidsample.shared.domain.TransferDirectionFilter
import com.spiraclesoftware.androidsample.utils.LiveDataTestUtil
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class TransactionListViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val accountRepository = AccountRepository()

    @Mock
    private lateinit var transactionsRepository: TransactionsRepository

    @Mock
    private lateinit var ratesRepository: ConversionRatesRepository

    private lateinit var viewModel: TransactionListViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        viewModel = TransactionListViewModel(accountRepository, transactionsRepository, ratesRepository)
    }

    @Test
    fun testNull() {
        assertNotNull(viewModel.listData)
        assertNotNull(viewModel.transactionListFilter)
        verify(transactionsRepository, never()).loadTransactionList(any())
    }

    @Test
    fun dontFetchWithoutObservers() {
        viewModel.setTransferDirectionFilter(TransferDirectionFilter.ALL)
        verify(transactionsRepository, never()).loadTransactionList(any())
    }

    @Test
    fun fetchWhenObserved() {
        viewModel.setTransferDirectionFilter(TransferDirectionFilter.ALL)
        viewModel.listData.observeForever(mock())
        verify(transactionsRepository).loadTransactionList(any())
    }

    @Test
    fun transactions() {
        viewModel.listData.observeForever(mock())

        verifyNoMoreInteractions(transactionsRepository)

        viewModel.setTransferDirectionFilter(TransferDirectionFilter.ALL)
        verify(transactionsRepository).loadTransactionList(any())
    }

    @Test
    fun refresh() {
        viewModel.setTransferDirectionFilter(TransferDirectionFilter.ALL)
        verifyNoMoreInteractions(transactionsRepository)
        verifyNoMoreInteractions(ratesRepository)

        viewModel.listData.observeForever(mock())
        verify(transactionsRepository).loadTransactionList(any())

        reset(transactionsRepository)
        viewModel.refresh()
        verify(transactionsRepository).loadTransactionList(any())
    }

    @Test
    fun navigateToTransactionDetail() {
        val transactionId = TransactionId(1)
        viewModel.openTransactionDetail(transactionId)

        val navigateEvent = LiveDataTestUtil.getValue(viewModel.navigateToDetailAction)
        assertEquals(transactionId, navigateEvent?.getContentIfNotHandled())
    }

    @Test
    fun navigateToRatesConverter() {
        viewModel.navigateToRatesConverterAction.observeForever(mock())

        viewModel.openRatesConverter()

        verify(viewModel.navigateToRatesConverterAction).observe(any(), any())

        val navigateEvent = LiveDataTestUtil.getValue(viewModel.navigateToDetailAction)
        assertEquals(true, navigateEvent?.hasBeenHandled)
    }
}