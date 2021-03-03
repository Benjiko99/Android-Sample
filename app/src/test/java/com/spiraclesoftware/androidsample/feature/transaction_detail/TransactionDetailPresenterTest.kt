package com.spiraclesoftware.androidsample.feature.transaction_detail

import android.net.Uri
import co.zsmb.rainbowcake.test.base.PresenterTest
import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.domain.Result
import com.spiraclesoftware.androidsample.domain.data
import com.spiraclesoftware.androidsample.domain.entity.*
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import com.spiraclesoftware.androidsample.epochDateTime
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.CardsFormatter
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.CardsPresenter
import com.spiraclesoftware.androidsample.formatter.ExceptionFormatter
import com.spiraclesoftware.androidsample.framework.PresenterException
import com.spiraclesoftware.androidsample.money
import io.mockk.MockKAnnotations
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

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionDetailPresenterTest : PresenterTest() {

    companion object {
        private val MOCK_TRANSACTION = Transaction(
            TransactionId("1"),
            "Paypal *Steam",
            epochDateTime,
            money("49.99", "EUR"),
            TransferDirection.OUTGOING,
            TransactionCategory.ENTERTAINMENT,
            TransactionStatus.COMPLETED,
            TransactionStatusCode.SUCCESSFUL,
        )
    }

    @MockK
    lateinit var transactionsInteractor: TransactionsInteractor

    @MockK
    lateinit var transactionDetailFormatter: TransactionDetailFormatter

    @MockK
    lateinit var cardsPresenter: CardsPresenter

    @MockK
    lateinit var cardsFormatter: CardsFormatter

    @MockK
    lateinit var exceptionFormatter: ExceptionFormatter

    @InjectMockKs
    lateinit var testSubject: TransactionDetailPresenter

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `Detail model is presented successfully`() = runBlockingTest {
        val mockDetailModel = mockk<DetailModel>()
        val attachmentUploads = flowOf(emptyList<Uri>())

        every { transactionDetailFormatter.detailModel(any(), any()) } returns mockDetailModel
        every { transactionsInteractor.flowTransactionById(any()) } returns flowOf(MOCK_TRANSACTION)
        every { transactionsInteractor.getTransactionById(any()) } returns MOCK_TRANSACTION
        every { cardsPresenter.getCards(any(), any()) } returns emptyList()
        every { cardsFormatter.cardModels(any()) } returns emptyList()

        val actual = testSubject.flowDetailModel(MOCK_TRANSACTION.id, attachmentUploads).first().data
        assertThat(actual).isEqualTo(mockDetailModel)
    }

    @Test
    fun `Detail model presents error when transaction is null`() = runBlockingTest {
        val attachmentUploads = flowOf(emptyList<Uri>())

        every { transactionsInteractor.getTransactionById(any()) } returns null
        every { transactionsInteractor.flowTransactionById(any()) } returns flowOf(null)
        every { exceptionFormatter.format(any()) } returns "Error message"

        val error = testSubject.flowDetailModel(MOCK_TRANSACTION.id, attachmentUploads).first() as Result.Error

        assertThat(error.exception).isInstanceOf(PresenterException::class.java)
        assertThat(error.exception.message).isEqualTo("Error message")
    }

    @Test
    fun `Transaction is returned from interactor by ID`() = runBlockingTest {
        every { transactionsInteractor.getTransactionById(any()) } returns MOCK_TRANSACTION

        val actual = testSubject.getTransactionById(MOCK_TRANSACTION.id)
        assertThat(actual).isEqualTo(MOCK_TRANSACTION)
    }

}
