package com.spiraclesoftware.androidsample.ui.transactionlist

import co.zsmb.rainbowcake.test.base.PresenterTest
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.spiraclesoftware.androidsample.TestData
import com.spiraclesoftware.androidsample.domain.interactor.AccountsInteractor
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.domain.model.TransactionListFilter
import com.spiraclesoftware.androidsample.domain.model.TransferDirectionFilter.ALL
import com.spiraclesoftware.androidsample.domain.model.TransferDirectionFilter.INCOMING_ONLY
import com.spiraclesoftware.androidsample.domain.model.applyFilter
import com.spiraclesoftware.androidsample.domain.policy.TransactionsPolicy
import com.spiraclesoftware.androidsample.money
import com.spiraclesoftware.core.utils.LanguageManager
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
        private val MOCK_TRANSACTIONS = TestData.transactions
    }

    @Mock
    private lateinit var languageManager: LanguageManager

    @Mock
    private lateinit var transactionsPolicy: TransactionsPolicy

    @Mock
    private lateinit var accountsInteractor: AccountsInteractor

    @Mock
    private lateinit var transactionsInteractor: TransactionsInteractor

    private lateinit var presenter: TransactionListPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = TransactionListPresenter(
            languageManager,
            transactionsPolicy,
            accountsInteractor,
            transactionsInteractor
        )
    }

    @Test
    fun `Account is returned from interactor`() = runBlockingTest {
        whenever(accountsInteractor.getAccount()) doReturn MOCK_ACCOUNT

        val account = presenter.getAccount()

        assertEquals(MOCK_ACCOUNT, account)
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
    fun `Transactions are returned from network when force fetching them`() = runBlockingTest {
        whenever(transactionsInteractor.fetchTransactions()) doReturn MOCK_TRANSACTIONS

        val filter = TransactionListFilter(ALL)
        val transactions = presenter.getTransactions(filter, forceFetch = true)

        verify(transactionsInteractor).fetchTransactions()
        assertEquals(MOCK_TRANSACTIONS, transactions)
    }

    @Test
    fun `All list items are returned from interactor`() = runBlockingTest {
        val mockTransactions = listOf(
            TestData.transactions[0],
            TestData.transactions[0]
        )

        val mockContribution = money("100", "EUR")

        whenever(accountsInteractor.getAccount()) doReturn MOCK_ACCOUNT
        whenever(
            transactionsPolicy.getContributionToBalance(any<List<Transaction>>(), any())
        ) doReturn mockContribution

        val listItems = presenter.getListItems(mockTransactions)

        val contributions = transactionsPolicy
            .getContributionToBalance(mockTransactions, MOCK_ACCOUNT.currency)

        val expectedListItems = listOf(
            HeaderItem(TestData.epochDateTime, contributions),
            TransactionItem(mockTransactions[0]),
            TransactionItem(mockTransactions[1])
        )

        assertEquals(expectedListItems, listItems)
    }

}
