package com.spiraclesoftware.androidsample.features.transaction_list

import co.zsmb.rainbowcake.test.base.PresenterTest
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import com.spiraclesoftware.androidsample.domain.interactor.AccountsInteractor
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import com.spiraclesoftware.androidsample.domain.model.*
import com.spiraclesoftware.androidsample.epochDateTime
import com.spiraclesoftware.androidsample.formatters.ExceptionFormatter
import com.spiraclesoftware.androidsample.money
import com.spiraclesoftware.androidsample.utils.LanguageManager
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionListPresenterTest : PresenterTest() {

    companion object {
        private val MOCK_ACCOUNT = Account(Currency.getInstance("EUR"))

        private val MOCK_TRANSACTIONS = listOf(
            Transaction(
                TransactionId("1"),
                "Paypal *Steam",
                epochDateTime,
                money("49.99", "EUR"),
                TransferDirection.OUTGOING,
                TransactionCategory.ENTERTAINMENT,
                TransactionStatus.COMPLETED,
                TransactionStatusCode.SUCCESSFUL,
            ),
            Transaction(
                TransactionId("2"),
                "Salary",
                epochDateTime,
                money("1000", "EUR"),
                TransferDirection.INCOMING,
                TransactionCategory.TRANSFERS,
                TransactionStatus.COMPLETED,
                TransactionStatusCode.SUCCESSFUL
            ),
        )
    }

    @Mock
    private lateinit var languageManager: LanguageManager

    @Mock
    private lateinit var accountsInteractor: AccountsInteractor

    @Mock
    private lateinit var transactionsInteractor: TransactionsInteractor

    @Mock
    private lateinit var exceptionFormatter: ExceptionFormatter

    private lateinit var presenter: TransactionListPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = TransactionListPresenter(
            languageManager,
            accountsInteractor,
            transactionsInteractor,
            exceptionFormatter
        )
    }

    @Test
    fun `Account is returned from interactor`() = runBlockingTest {
        whenever(accountsInteractor.getAccount()) doReturn MOCK_ACCOUNT

        val account = presenter.getAccount()

        assertEquals(MOCK_ACCOUNT, account)
    }

    @Test
    fun `All list items are returned from interactor`() = runBlockingTest {
        val mockTransactions = listOf(
            MOCK_TRANSACTIONS[0],
            MOCK_TRANSACTIONS[0]
        )

        val mockContribution = money("100", "EUR")

        whenever(accountsInteractor.getAccount()) doReturn MOCK_ACCOUNT
        whenever(accountsInteractor.getContributionToBalance(any())) doReturn mockContribution

        val listItems = presenter.getListItems(mockTransactions)

        val contribution = accountsInteractor.getContributionToBalance(mockTransactions)

        val expectedListItems = listOf(
            HeaderItem(epochDateTime, contribution),
            TransactionItem(mockTransactions[0]),
            TransactionItem(mockTransactions[1])
        )

        assertEquals(expectedListItems, listItems)
    }

}
