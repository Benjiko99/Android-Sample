package com.spiraclesoftware.androidsample.feature.transaction_detail.cards

import co.zsmb.rainbowcake.test.base.PresenterTest
import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.domain.entity.*
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test
import java.time.ZonedDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class CardsPresenterTest : PresenterTest() {

    @MockK
    lateinit var cardsFormatter: CardsFormatter

    @InjectMockKs
    lateinit var cardsPresenter: CardsPresenter

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun presentCardsForTransaction() {
        val transaction = Transaction(
            TransactionId("1"),
            "Paypal *Steam",
            ZonedDateTime.now(),
            Money("49.99", "EUR"),
            TransferDirection.OUTGOING,
            TransactionCategory.ENTERTAINMENT,
            TransactionStatus.COMPLETED,
            TransactionStatusCode.SUCCESSFUL,
            emptyList(),
            "VISA **9400",
            "Note to self"
        )

        val cards = cardsPresenter.getCards(transaction, emptyList())

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
