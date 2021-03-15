package com.spiraclesoftware.androidsample.feature.transaction_list

import co.zsmb.rainbowcake.test.base.PresenterTest
import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.domain.Result
import com.spiraclesoftware.androidsample.domain.data
import com.spiraclesoftware.androidsample.domain.entity.Transaction
import com.spiraclesoftware.androidsample.domain.entity.TransactionsFilter
import com.spiraclesoftware.androidsample.domain.interactor.AccountsInteractor
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import com.spiraclesoftware.androidsample.epochDateTime
import com.spiraclesoftware.androidsample.feature.transaction_list.item.model.HeaderModel
import com.spiraclesoftware.androidsample.feature.transaction_list.item.model.TransactionModel
import com.spiraclesoftware.androidsample.format.ExceptionFormatter
import com.spiraclesoftware.androidsample.framework.PresenterException
import com.spiraclesoftware.androidsample.util.LanguageManager
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionListPresenterTest : PresenterTest() {

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
    lateinit var testSubject: TransactionListPresenter

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun toggleLanguage() = runBlockingTest {
        justRun { languageManager.toggleLanguageAndRestart() }

        testSubject.toggleLanguageAndRestart()

        verify { languageManager.toggleLanguageAndRestart() }
    }

    @Test
    fun refreshTransactions() = runBlockingTest {
        coJustRun { transactionsInteractor.refreshTransactions() }

        transactionsInteractor.refreshTransactions()

        coVerify { transactionsInteractor.refreshTransactions() }
    }

    @Test
    fun presentContentModel() = runBlockingTest {
        val transaction = mockk<Transaction> {
            every { processingDate } returns epochDateTime
        }
        val dataResult = Result.Success(listOf(transaction))
        val mockHeaderModel = mockk<HeaderModel>()
        val mockTransactionModels = listOf(mockk<TransactionModel>())
        val mockFilterModel = mockk<FilterModel>()
        every { transactionsInteractor.flowTransactions(any()) } returns flowOf(dataResult)
        every { transactionListFormatter.headerModel(any(), any()) } returns mockHeaderModel
        every { transactionListFormatter.transactionModel(any<List<Transaction>>()) } returns mockTransactionModels
        every { transactionListFormatter.filterModel(any()) } returns mockFilterModel
        every { transactionListFormatter.emptyState(any(), any()) } returns null
        coEvery { accountsInteractor.getContributionToBalance(any()) } returns mockk()

        val filterFlow = flowOf(TransactionsFilter())
        val actual = testSubject.flowContentModel(filterFlow).first().data

        val expectedModels = listOf(mockHeaderModel) + mockTransactionModels
        val expected = ContentModel(expectedModels, mockFilterModel, emptyState = null)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun presentContentModel_emptyState() = runBlockingTest {
        val dataResult = Result.Success(emptyList<Transaction>())
        val mockFilterModel = mockk<FilterModel>()
        val mockEmptyState = mockk<EmptyState>()
        every { transactionsInteractor.flowTransactions(any()) } returns flowOf(dataResult)
        every { transactionListFormatter.filterModel(any()) } returns mockFilterModel
        every { transactionListFormatter.emptyState(any(), any()) } returns mockEmptyState

        val filterFlow = flowOf(TransactionsFilter())
        val actual = testSubject.flowContentModel(filterFlow).first().data

        val expected = ContentModel(emptyList(), mockFilterModel, mockEmptyState)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun whenErrorPresentingContentModel_thenPresenterErrorIsThrown() = runBlockingTest {
        val dataResult = Result.Error(Exception())
        every { transactionsInteractor.flowTransactions(any()) } returns flowOf(dataResult)
        every { exceptionFormatter.format(any()) } returns "Error"

        val filterFlow = flowOf(TransactionsFilter())
        val error = testSubject.flowContentModel(filterFlow).first() as Result.Error

        assertThat(error.exception).isInstanceOf(PresenterException::class.java)
        assertThat(error.exception.message).isEqualTo("Error")
    }

}
