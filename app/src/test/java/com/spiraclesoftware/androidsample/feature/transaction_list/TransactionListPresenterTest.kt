package com.spiraclesoftware.androidsample.feature.transaction_list

import co.zsmb.rainbowcake.test.base.PresenterTest
import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.domain.entity.Account
import com.spiraclesoftware.androidsample.domain.entity.Transaction
import com.spiraclesoftware.androidsample.domain.entity.TransactionId
import com.spiraclesoftware.androidsample.domain.interactor.AccountsInteractor
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import com.spiraclesoftware.androidsample.epochDateTime
import com.spiraclesoftware.androidsample.formatter.ExceptionFormatter
import com.spiraclesoftware.androidsample.money
import com.spiraclesoftware.androidsample.util.LanguageManager
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    lateinit var headerModelFormatter: HeaderModelFormatter

    @MockK
    lateinit var transactionModelFormatter: TransactionModelFormatter

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
    fun `Models are presented correctly`() = runBlockingTest {
        val transaction = mockk<Transaction> {
            every { processingDate } returns epochDateTime
        }
        val transactions = listOf(transaction)

        val headerModel = mockk<HeaderModel>()
        val transactionModel = mockk<TransactionModel> {
            every { id } returns TransactionId("1")
        }

        val mockContribution = money("100", "EUR")

        every { accountsInteractor.getAccount() } returns MOCK_ACCOUNT
        coEvery { accountsInteractor.getContributionToBalance(any()) } returns mockContribution
        every { headerModelFormatter.format(any(), any()) } returns headerModel
        every { transactionModelFormatter.format(transactions) } returns listOf(transactionModel)

        val models = presenter.getListModels(transactions)
        val expectedModels = listOf(headerModel, transactionModel)

        assertThat(models).isEqualTo(expectedModels)
    }

}
