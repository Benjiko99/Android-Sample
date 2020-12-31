package com.spiraclesoftware.androidsample.ui.transactiondetail.cards

import co.zsmb.rainbowcake.test.base.PresenterTest
import com.spiraclesoftware.androidsample.domain.model.*
import com.spiraclesoftware.androidsample.epochDateTime
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
            epochDateTime,
            money("49.99", "EUR"),
            TransferDirection.OUTGOING,
            TransactionCategory.ENTERTAINMENT,
            TransactionStatus.COMPLETED,
            TransactionStatusCode.SUCCESSFUL,
            emptyList(),
            "VISA **9400",
            "Note to self"
        )

        val presenter = CardsPresenter()
        val cards = presenter.getCards(transaction)

        assertEquals(4, cards.count())

        assert(cards[0] is ValuePairCard)
        assert(cards[1] is CategoryCard)
        assert(cards[2] is AttachmentsCard)
        assert(cards[3] is NoteCard)

        ((cards[0] as ValuePairCard).valuePairs).let { valuePairs ->
            assert(valuePairs[0] is ValuePairs.Status)
            assert(valuePairs[1] is ValuePairs.CardDescription)
            assert(valuePairs[2] is ValuePairs.DownloadStatement)
        }
    }

}
