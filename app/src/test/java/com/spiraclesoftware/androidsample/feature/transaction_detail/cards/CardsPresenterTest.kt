package com.spiraclesoftware.androidsample.feature.transaction_detail.cards

import co.zsmb.rainbowcake.test.base.PresenterTest
import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.domain.entity.*
import com.spiraclesoftware.androidsample.epochDateTime
import com.spiraclesoftware.androidsample.money
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CardsPresenterTest : PresenterTest() {

    @Test
    fun presentCardsForTransaction() {
        val transaction = Transaction(
            TransactionId("1"),
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
        val cards = presenter.getCards(transaction, emptyList())

        assertThat(cards.count()).isEqualTo(4)

        assertThat(cards[0]).isInstanceOf(ValuePairCard::class.java)
        assertThat(cards[1]).isInstanceOf(CategoryCard::class.java)
        assertThat(cards[2]).isInstanceOf(AttachmentsCard::class.java)
        assertThat(cards[3]).isInstanceOf(NoteCard::class.java)

        ((cards[0] as ValuePairCard).valuePairs).let { valuePairs ->
            assertThat(valuePairs[0]).isInstanceOf(ValuePair.Status::class.java)
            assertThat(valuePairs[1]).isInstanceOf(ValuePair.CardDescription::class.java)
            assertThat(valuePairs[2]).isInstanceOf(ValuePair.DownloadStatement::class.java)
        }
    }

}
