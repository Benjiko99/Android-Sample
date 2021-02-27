package com.spiraclesoftware.androidsample.feature.transaction_detail

import co.zsmb.rainbowcake.test.base.PresenterTest
import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.domain.entity.*
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import com.spiraclesoftware.androidsample.epochDateTime
import com.spiraclesoftware.androidsample.money
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    lateinit var transactionDetailFormatter: TransactionDetailFormatter

    @MockK
    lateinit var transactionsInteractor: TransactionsInteractor

    @InjectMockKs
    lateinit var testSubject: TransactionDetailPresenter

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `Transaction is returned from interactor by ID`() = runBlockingTest {
        every { transactionsInteractor.getTransactionById(MOCK_TRANSACTION.id) } returns MOCK_TRANSACTION

        val transaction = testSubject.getTransactionById(MOCK_TRANSACTION.id)
        assertThat(transaction).isEqualTo(MOCK_TRANSACTION)
    }

}
