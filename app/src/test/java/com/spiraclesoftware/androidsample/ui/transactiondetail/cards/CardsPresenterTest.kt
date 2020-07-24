package com.spiraclesoftware.androidsample.ui.transactiondetail.cards

import co.zsmb.rainbowcake.test.base.PresenterTest
import com.spiraclesoftware.androidsample.TestData
import com.spiraclesoftware.androidsample.domain.model.*
import com.spiraclesoftware.androidsample.money
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CardsPresenterTest : PresenterTest() {

    @Test
    fun `Correct cards are generated for transaction`() {
        val transaction = Transaction(
            TransactionId(1),
            "Paypal *Steam",
            TestData.epochDateTime,
            money("49.99", "EUR"),
            TransferDirection.OUTGOING,
            TransactionCategory.ENTERTAINMENT,
            TransactionStatus.COMPLETED,
            TransactionStatusCode.SUCCESSFUL,
            "VISA **9400",
            "Note to self"
        )

        val presenter = CardsPresenter()
        val cards = presenter.getCardsFor(transaction)

        assertEquals(3, cards.count())

        assert(cards[0].valuePairs[0] is CardValuePairs.TransactionStatus)
        assert(cards[0].valuePairs[1] is CardValuePairs.CardDescription)
        assert(cards[0].valuePairs[2] is CardValuePairs.TransactionStatement)

        assert(cards[1].valuePairs[0] is CardValuePairs.TransactionCategory)

        assert(cards[2].valuePairs[0] is CardValuePairs.NoteToSelf)
    }

}
