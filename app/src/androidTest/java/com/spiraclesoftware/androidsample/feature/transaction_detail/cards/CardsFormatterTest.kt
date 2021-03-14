package com.spiraclesoftware.androidsample.feature.transaction_detail.cards

import androidx.core.net.toUri
import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.domain.entity.TransactionCategory
import com.spiraclesoftware.androidsample.domain.entity.TransactionStatus
import com.spiraclesoftware.androidsample.domain.entity.TransactionStatusCode
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.item.model.*
import com.spiraclesoftware.androidsample.format.FormatterTest
import com.spiraclesoftware.androidsample.framework.StringHolder
import org.junit.Test

class CardsFormatterTest : FormatterTest() {

    @Test
    fun formatCardModels() {
        // TODO: Use cards DSL?
        val cards = listOf(
            ValuePairCard(
                listOf(
                    ValuePair.Status(TransactionStatus.COMPLETED),
                    // TODO: CardActionsHandler is not testable,
                    //  the below ValuePairs require it
                    //ValuePair.CardDescription,
                    //ValuePair.DownloadStatement,
                )
            ),
            StatusCard(
                TransactionStatus.COMPLETED,
                TransactionStatusCode.SUCCESSFUL
            ),
            CategoryCard(TransactionCategory.ENTERTAINMENT),
            AttachmentsCard(
                listOf("https://example.com/image.png"),
                listOf("file://storage/emulated/0/DCIM/Camera/photo.jpg".toUri())
            ),
            NoteCard("Note"),
        )

        val uploads = listOf("file://storage/emulated/0/DCIM/Camera/photo.jpg".toUri())

        val expected = listOf(
            ValuePairCardModel(
                listOf(
                    ValuePairModel(
                        label = R.string.transaction_detail__status,
                        value = StringHolder(R.string.transaction_status__completed)
                    ),
//                    ValuePairModel(
//                        label = R.string.transaction_detail__card,
//                        value = StringHolder("Mastercard"),
//                        icon = R.drawable.ic_credit_card,
//                        onClickAction = { it.onOpenCardDetail() }
//                    ),
//                    ValuePairModel(
//                        label = R.string.transaction_detail__statement,
//                        value = StringHolder(R.string.transaction_detail__download),
//                        icon = R.drawable.ic_download_statement,
//                        onClickAction = { it.onDownloadStatement() }
//                    )
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

        val formatter = CardsFormatter()
        val actual = formatter.cardModels(cards)

        assertThat(actual).isEqualTo(expected)
    }

}