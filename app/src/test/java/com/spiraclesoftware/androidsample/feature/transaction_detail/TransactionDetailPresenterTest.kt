package com.spiraclesoftware.androidsample.feature.transaction_detail

import android.net.Uri
import co.zsmb.rainbowcake.test.base.PresenterTest
import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.common.formatter.ExceptionFormatter
import com.spiraclesoftware.androidsample.domain.entity.*
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.CardsPresenter
import com.spiraclesoftware.androidsample.framework.core.Model
import com.spiraclesoftware.androidsample.framework.core.PresenterException
import io.mockk.*
import io.mockk.impl.annotations.MockK
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

    @MockK
    lateinit var transactionsInteractor: TransactionsInteractor

    @MockK
    lateinit var transactionDetailFormatter: TransactionDetailFormatter

    @MockK
    lateinit var cardsPresenter: CardsPresenter

    @MockK
    lateinit var exceptionFormatter: ExceptionFormatter

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        coEvery { transactionsInteractor.flowTransactionById(any()) } returns mockk()
    }

    private fun newTestSubject() =
        TransactionDetailPresenter(
            TRANSACTION.id,
            transactionsInteractor,
            transactionDetailFormatter,
            cardsPresenter,
            exceptionFormatter
        )

    @Test
    fun `Present detail model`() = runBlockingTest {
        val mockDetailModel = mockk<DetailModel>()

        every { transactionDetailFormatter.detailModel(any()) } returns mockDetailModel
        every { transactionsInteractor.flowTransactionById(any()) } returns flowOf(TRANSACTION)

        val actual = newTestSubject().flowDetailModel().first()
        assertThat(actual).isEqualTo(mockDetailModel)
    }

    @Test
    fun `Exception when presenting detail model is formatted and rethrown`() = runBlockingTest {
        every { transactionsInteractor.flowTransactionById(any()) } returns flowOf(null)
        every { exceptionFormatter.format(any()) } returns "error"

        val exception = assertThrows(PresenterException::class.java) {
            runBlocking {
                newTestSubject().flowDetailModel().first()
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

        val actual = newTestSubject().flowCardModels(attachmentUploads).first()
        assertThat(actual).isEqualTo(cardModels)
        assertThat(true).isEqualTo(true)
    }

    @Test
    fun `Exception when presenting card models is formatted and rethrown`() = runBlockingTest {
        every { transactionsInteractor.flowTransactionById(any()) } returns flowOf(null)
        every { exceptionFormatter.format(any()) } returns "error"

        val exception = assertThrows(PresenterException::class.java) {
            runBlocking {
                newTestSubject().flowCardModels(flowOf(emptyList())).first()
            }
        }
        assertThat(exception.message).isEqualTo("error")
    }

    @Test
    fun `Get action chips`() = runBlockingTest {
        val expected = listOf(ActionChip(R.id.action_split_bill, isVisible = true))

        every { transactionsInteractor.flowTransactionById(any()) } returns flowOf(
            mockk {
                every { isSuccessful() } returns true
                every { transferDirection } returns TransferDirection.OUTGOING
                every { category } returns TransactionCategory.ENTERTAINMENT
            }
        )

        val actual = newTestSubject().getActionChips()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `Update note`() = runBlockingTest {
        val note = "abc"

        coEvery { transactionsInteractor.flowTransactionById(any()) } returns mockk()
        coEvery { transactionsInteractor.updateTransactionNote(any(), any()) } returns mockk {
            every { noteToSelf } returns note
        }

        val updatedTransaction = newTestSubject().updateNote(note)
        val actual = updatedTransaction.noteToSelf

        assertThat(actual).isEqualTo(note)
    }

    @Test
    fun `Upload attachment`() = runBlockingTest {
        val attachment = "abc"

        coJustRun { transactionsInteractor.uploadAttachment(any(), any()) }

        newTestSubject().uploadAttachment(attachment)

        coVerify { transactionsInteractor.uploadAttachment(TRANSACTION.id, attachment) }
    }

    @Test
    fun `Remove attachment`() = runBlockingTest {
        val attachment = "abc"

        coJustRun { transactionsInteractor.removeAttachment(any(), any()) }

        newTestSubject().removeAttachment(attachment)

        coVerify { transactionsInteractor.removeAttachment(TRANSACTION.id, attachment) }
    }

    @Test
    fun `Get attachments`() = runBlockingTest {
        val expected = mockk<List<String>>()

        coEvery { transactionsInteractor.flowTransactionById(any()) } returns flowOf(
            mockk {
                every { attachments } returns expected
            }
        )

        val actual = newTestSubject().getAttachments()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `Get note`() = runBlockingTest {
        val expected = "abc"

        coEvery { transactionsInteractor.flowTransactionById(any()) } returns flowOf(
            mockk {
                every { noteToSelf } returns expected
            }
        )

        val actual = newTestSubject().getNote()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `Get category`() = runBlockingTest {
        val expected = TransactionCategory.ENTERTAINMENT

        coEvery { transactionsInteractor.flowTransactionById(any()) } returns flowOf(
            mockk {
                every { category } returns expected
            }
        )

        val actual = newTestSubject().getCategory()

        assertThat(actual).isEqualTo(expected)
    }

}
