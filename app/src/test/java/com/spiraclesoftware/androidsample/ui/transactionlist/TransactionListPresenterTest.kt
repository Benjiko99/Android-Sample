package com.spiraclesoftware.androidsample.ui.transactionlist

import co.zsmb.rainbowcake.test.base.PresenterTest
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import com.spiraclesoftware.androidsample.TestData
import com.spiraclesoftware.androidsample.domain.interactor.AccountsInteractor
import com.spiraclesoftware.androidsample.domain.interactor.ConversionRatesInteractor
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import com.spiraclesoftware.androidsample.domain.model.*
import com.spiraclesoftware.androidsample.domain.model.TransferDirectionFilter.ALL
import com.spiraclesoftware.androidsample.domain.model.TransferDirectionFilter.INCOMING_ONLY
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
        private val MOCK_ACCOUNT = TestData.account
        private val MOCK_RATES = TestData.conversionRates
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
    fun `Account is returned from interactor`() = runBlockingTest {
        whenever(accountsInteractor.getAccount()) doReturn MOCK_ACCOUNT

        val account = presenter.getAccount()

        assertEquals(MOCK_ACCOUNT, account)
    }

    @Test
    fun `Conversion rates are returned from interactor`() = runBlockingTest {
        whenever(accountsInteractor.getAccount()) doReturn MOCK_ACCOUNT
        whenever(conversionRatesInteractor.getConversionRates(any(), any())) doReturn MOCK_RATES

        val rates = presenter.getConversionRates()

        assertEquals(MOCK_RATES, rates)
    }

    @Test
    fun `All transactions are returned from interactor`() = runBlockingTest {
        whenever(transactionsInteractor.getTransactions()) doReturn MOCK_TRANSACTIONS

        val filter = TransactionListFilter(ALL)
        val transactions = presenter.getTransactions(filter)

        assertEquals(MOCK_TRANSACTIONS, transactions)
    }

    @Test
    fun `Filtered transactions are returned from interactor`() = runBlockingTest {
        whenever(transactionsInteractor.getTransactions()) doReturn MOCK_TRANSACTIONS

        val filter = TransactionListFilter(INCOMING_ONLY)
        val transactions = presenter.getTransactions(filter)

        assertEquals(MOCK_TRANSACTIONS.applyFilter(filter), transactions)
    }

    @Test
    fun `All list items are returned from interactor`() = runBlockingTest {
        val mockTransactions = listOf(
            TestData.transactions[0],
            TestData.transactions[0]
        )

        whenever(accountsInteractor.getAccount()) doReturn MOCK_ACCOUNT
        whenever(transactionsInteractor.getTransactions()) doReturn mockTransactions
        whenever(conversionRatesInteractor.getConversionRates(any(), any())) doReturn MOCK_RATES

        val filter = TransactionListFilter(ALL)
        val listItems = presenter.getListItems(filter)

        val contributions = mockTransactions.getContributionsToBalance(MOCK_RATES, MOCK_ACCOUNT.currency)

        val expectedListItems = listOf(
            HeaderItem(TestData.epochDateTime, contributions),
            TransactionItem(mockTransactions[0]),
            TransactionItem(mockTransactions[1])
        )

        assertEquals(expectedListItems, listItems)
    }

}
