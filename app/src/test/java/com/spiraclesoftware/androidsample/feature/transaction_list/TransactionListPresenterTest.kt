package com.spiraclesoftware.androidsample.feature.transaction_list

import co.zsmb.rainbowcake.test.base.PresenterTest
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.spiraclesoftware.androidsample.domain.entity.Account
import com.spiraclesoftware.androidsample.domain.entity.Transaction
import com.spiraclesoftware.androidsample.domain.entity.TransactionId
import com.spiraclesoftware.androidsample.domain.interactor.AccountsInteractor
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import com.spiraclesoftware.androidsample.epochDateTime
import com.spiraclesoftware.androidsample.formatter.ExceptionFormatter
import com.spiraclesoftware.androidsample.money
import com.spiraclesoftware.androidsample.util.LanguageManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionListPresenterTest : PresenterTest() {

    companion object {
        private val MOCK_ACCOUNT = Account(Currency.getInstance("EUR"))
    }

    @Mock
    private lateinit var languageManager: LanguageManager

    @Mock
    private lateinit var accountsInteractor: AccountsInteractor

    @Mock
    private lateinit var transactionsInteractor: TransactionsInteractor

    @Mock
    private lateinit var headerModelFormatter: HeaderModelFormatter

    @Mock
    private lateinit var transactionModelFormatter: TransactionModelFormatter

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
            headerModelFormatter,
            transactionModelFormatter,
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
    fun `Models are presented correctly`() = runBlockingTest {
        val transaction = mock<Transaction> {
            whenever(it.processingDate) doReturn epochDateTime
        }
        val transactions = listOf(transaction)

        val headerModel = mock<HeaderModel>()
        val transactionModel = mock<TransactionModel> {
            whenever(it.id) doReturn TransactionId("1")
        }

        val mockContribution = money("100", "EUR")

        whenever(accountsInteractor.getAccount()) doReturn MOCK_ACCOUNT
        whenever(accountsInteractor.getContributionToBalance(any())) doReturn mockContribution
        whenever(headerModelFormatter.format(any(), any())) doReturn headerModel
        whenever(transactionModelFormatter.format(transactions)) doReturn listOf(transactionModel)

        val models = presenter.getListModels(transactions)
        val expectedModels = listOf(headerModel, transactionModel)

        assertEquals(expectedModels, models)
    }

}
