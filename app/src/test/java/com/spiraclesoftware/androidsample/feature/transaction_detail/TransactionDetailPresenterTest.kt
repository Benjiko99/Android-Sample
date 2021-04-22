package com.spiraclesoftware.androidsample.feature.transaction_detail

import android.net.Uri
import co.zsmb.rainbowcake.test.base.PresenterTest
import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.domain.entity.*
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.CardsPresenter
import com.spiraclesoftware.androidsample.format.ExceptionFormatter
import com.spiraclesoftware.androidsample.framework.Model
import com.spiraclesoftware.androidsample.framework.PresenterException
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.time.ZonedDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionDetailPresenterTest : PresenterTest() {

    companion object {
        private val TRANSACTION = Transaction(
            TransactionId("1"),
            "Paypal *Steam",
            ZonedDateTime.now(),
            Money("49.99", "EUR"),
            TransferDirection.OUTGOING,
            TransactionCategory.ENTERTAINMENT,
            TransactionStatus.COMPLETED,
            TransactionStatusCode.SUCCESSFUL,
        )
    }

    // gets injected as constructor param into the [testSubject]
    val transactionId = TransactionId("1")

    @MockK
    lateinit var transactionsInteractor: TransactionsInteractor

    @MockK
    lateinit var transactionDetailFormatter: TransactionDetailFormatter

    @MockK
    lateinit var cardsPresenter: CardsPresenter

    @MockK
    lateinit var exceptionFormatter: ExceptionFormatter

    @InjectMockKs
    lateinit var testSubject: TransactionDetailPresenter

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `Present detail model`() = runBlockingTest {
        val mockDetailModel = mockk<DetailModel>()

        every { transactionDetailFormatter.detailModel(any()) } returns mockDetailModel
        every { transactionsInteractor.flowTransactionById(any()) } returns flowOf(TRANSACTION)

        val actual = testSubject.flowDetailModel().first()
        assertThat(actual).isEqualTo(mockDetailModel)
    }

    @Test
    fun `Exception when presenting detail model is formatted and rethrown`() = runBlockingTest {
        every { transactionsInteractor.flowTransactionById(any()) } returns flowOf(null)
        every { exceptionFormatter.format(any()) } returns "error"

        val exception = assertThrows(PresenterException::class.java) {
            runBlocking {
                testSubject.flowDetailModel().first()
            }
        }
        assertThat(exception.message).isEqualTo("error")
    }

    @Test
    fun `Present card models`() = runBlockingTest {
        val cardModels = emptyList<Model>()
        val attachmentUploads = flowOf(emptyList<Uri>())

        every { transactionsInteractor.flowTransactionById(any()) } returns flowOf(TRANSACTION)
        every { cardsPresenter.getCardModels(any(), any()) } returns cardModels

        val actual = testSubject.flowCardModels(attachmentUploads).first()
        assertThat(actual).isEqualTo(cardModels)
        assertThat(true).isEqualTo(true)
    }

    @Test
    fun `Exception when presenting card models is formatted and rethrown`() = runBlockingTest {
        every { transactionsInteractor.flowTransactionById(any()) } returns flowOf(null)
        every { exceptionFormatter.format(any()) } returns "error"

        val exception = assertThrows(PresenterException::class.java) {
            runBlocking {
                testSubject.flowCardModels(flowOf(emptyList())).first()
            }
        }
        assertThat(exception.message).isEqualTo("error")
    }

}
