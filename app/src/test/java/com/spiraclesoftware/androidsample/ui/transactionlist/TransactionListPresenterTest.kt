package com.spiraclesoftware.androidsample.ui.transactionlist

import co.zsmb.rainbowcake.test.base.PresenterTest
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import com.spiraclesoftware.androidsample.TestData
import com.spiraclesoftware.androidsample.shared.domain.*
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionListPresenterTest : PresenterTest() {

    companion object {
        private val MOCK_TRANSACTIONS = TestData.transactions
    }

    @Mock
    private lateinit var accountsInteractor: AccountsInteractor

    @Mock
    private lateinit var transactionsInteractor: TransactionsInteractor

    @Mock
    private lateinit var conversionRatesInteractor: ConversionRatesInteractor

    private lateinit var presenter: TransactionListPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = TransactionListPresenter(accountsInteractor, transactionsInteractor, conversionRatesInteractor)
    }

    @Test
    fun `Unfiltered transactions are returned from interactor`() = runBlockingTest {
        whenever(transactionsInteractor.getTransactions()) doReturn MOCK_TRANSACTIONS

        val filter = TransactionListFilter(TransferDirectionFilter.ALL)
        val transactions = presenter.getTransactions(filter)
        assertEquals(MOCK_TRANSACTIONS, transactions)
    }

    @Test
    fun `Filtered transactions are returned from interactor`() = runBlockingTest {
        whenever(transactionsInteractor.getTransactions()) doReturn MOCK_TRANSACTIONS

        val filter = TransactionListFilter(TransferDirectionFilter.INCOMING_ONLY)
        val transactions = presenter.getTransactions(filter)
        assertEquals(MOCK_TRANSACTIONS.applyFilter(filter), transactions)
    }

}
