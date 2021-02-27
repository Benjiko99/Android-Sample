package com.spiraclesoftware.androidsample.feature.transaction_list

import co.zsmb.rainbowcake.test.base.PresenterTest
import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.domain.Result
import com.spiraclesoftware.androidsample.domain.data
import com.spiraclesoftware.androidsample.domain.entity.Account
import com.spiraclesoftware.androidsample.domain.entity.Transaction
import com.spiraclesoftware.androidsample.domain.entity.TransactionId
import com.spiraclesoftware.androidsample.domain.interactor.AccountsInteractor
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import com.spiraclesoftware.androidsample.epochDateTime
import com.spiraclesoftware.androidsample.feature.transaction_list.TransactionListViewModel.ViewData
import com.spiraclesoftware.androidsample.formatter.ExceptionFormatter
import com.spiraclesoftware.androidsample.framework.PresenterException
import com.spiraclesoftware.androidsample.money
import com.spiraclesoftware.androidsample.util.LanguageManager
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionListPresenterTest : PresenterTest() {

    companion object {
        private val MOCK_ACCOUNT = Account(Currency.getInstance("EUR"))
    }

    @MockK
    lateinit var languageManager: LanguageManager

    @MockK
    lateinit var accountsInteractor: AccountsInteractor

    @MockK
    lateinit var transactionsInteractor: TransactionsInteractor

    @MockK
    lateinit var transactionListFormatter: TransactionListFormatter

    @MockK
    lateinit var exceptionFormatter: ExceptionFormatter

    @InjectMockKs
    lateinit var presenter: TransactionListPresenter

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `Account is returned from interactor`() = runBlockingTest {
        every { accountsInteractor.getAccount() } returns MOCK_ACCOUNT

        val account = presenter.getAccount()

        assertThat(account).isEqualTo(MOCK_ACCOUNT)
    }

    @Test
    fun `Data is presented correctly`() = runBlockingTest {
        val mockContribution = money("100", "EUR")
        every { accountsInteractor.getAccount() } returns MOCK_ACCOUNT
        coEvery { accountsInteractor.getContributionToBalance(any()) } returns mockContribution

        val transaction = mockk<Transaction> {
            every { processingDate } returns epochDateTime
        }
        val transactionModel = mockk<TransactionModel> {
            every { id } returns TransactionId("1")
        }
        val transactions = listOf(transaction)
        val transactionModels = listOf(transactionModel)
        val headerModel = mockk<HeaderModel>()
        val result = Result.Success(transactions)
        val listFilter = flowOf(TransactionListFilter())
        every { transactionsInteractor.flowTransactions() } returns flowOf(result)
        every { transactionListFormatter.headerModel(any(), any()) } returns headerModel
        every { transactionListFormatter.transactionModel(transactions) } returns transactionModels
        every { transactionListFormatter.emptyState(any(), any()) } returns null
        every { exceptionFormatter.format(any()) } returns "Error message"

        val viewData = presenter.flowViewData(listFilter).first().data
        val expectedModels = listOf(headerModel) + transactionModels
        val expectedViewData = ViewData(expectedModels, TransactionListFilter(), emptyState = null)

        assertThat(viewData).isEqualTo(expectedViewData)
    }

    @Test
    fun `Error when presenting data`() = runBlockingTest {
        val result = Result.Error(Exception())
        val listFilter = flowOf(TransactionListFilter())
        every { transactionsInteractor.flowTransactions() } returns flowOf(result)
        every { exceptionFormatter.format(any()) } returns "Error message"

        val error = presenter.flowViewData(listFilter).first() as Result.Error

        assertThat(error.exception).isInstanceOf(PresenterException::class.java)
        assertThat(error.exception.message).isEqualTo("Error message")
    }

}