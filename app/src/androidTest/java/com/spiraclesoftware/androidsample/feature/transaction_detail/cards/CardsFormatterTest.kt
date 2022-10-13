package com.spiraclesoftware.androidsample.feature.transaction_detail.cards

import androidx.core.net.toUri
import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.common.formatter.FormatterTest
import com.spiraclesoftware.androidsample.domain.entity.TransactionCategory
import com.spiraclesoftware.androidsample.domain.entity.TransactionStatus
import com.spiraclesoftware.androidsample.domain.entity.TransactionStatusCode
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.item.model.*
import org.junit.Test
import org.koin.core.component.inject

class CardsFormatterTest : FormatterTest() {

    private val formatter by inject<CardsFormatter>()

    @Test
    fun formatCardModels() {
        val cards = cards {
            valuePairCard {
                status(TransactionStatus.COMPLETED)
                cardDescription("Mastercard")
                downloadStatement()
            }

            statusCard(
                TransactionStatus.COMPLETED,
                TransactionStatusCode.SUCCESSFUL
            )

            categoryCard(TransactionCategory.ENTERTAINMENT)

            attachmentsCard(
                listOf("https://example.com/image.png"),
                listOf("file://storage/emulated/0/DCIM/Camera/photo.jpg".toUri())
            )

            noteCard("Note")
        }

        val expected = listOf(
            ValuePairCardModel(
                listOf(
                    ValuePairModel(
                        label = R.string.transaction_detail__status,
                        value = "Completed"
                    ),
                    ValuePairModel(
                        label = R.string.transaction_detail__card,
                        value = "Mastercard",
                        icon = R.drawable.ic_credit_card,
                        actionId = R.id.action_open_card_detail
                    ),
                    ValuePairModel(
                        label = R.string.transaction_detail__statement,
                        value = "Download",
                        icon = R.drawable.ic_download_statement,
                        actionId = R.id.action_download_statement
                    )
                )
            ),
            StatusCardModel(
                R.string.transaction_status__completed,
                null
            ),
            CategoryCardModel(
                R.string.transaction_category__entertainment,
                R.drawable.ic_category_entertainment
            ),
            AttachmentsCardModel(
                listOf("https://example.com/image.png".toUri()),
                listOf("file://storage/emulated/0/DCIM/Camera/photo.jpg".toUri())
            ),
            NoteCardModel("Note"),
        )

        val actual = formatter.cardModels(cards)

        assertThat(actual).isEqualTo(expected)
    }

}