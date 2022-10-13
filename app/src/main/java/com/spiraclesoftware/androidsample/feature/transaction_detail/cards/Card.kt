package com.spiraclesoftware.androidsample.feature.transaction_detail.cards

import android.net.Uri
import com.spiraclesoftware.androidsample.domain.entity.TransactionCategory
import com.spiraclesoftware.androidsample.domain.entity.TransactionStatus
import com.spiraclesoftware.androidsample.domain.entity.TransactionStatusCode

sealed class Card

class ValuePairCard(val valuePairs: List<ValuePair>) : Card()

data class StatusCard(
    val status: TransactionStatus,
    val statusCode: TransactionStatusCode
) : Card()

data class CategoryCard(
    val category: TransactionCategory
) : Card()

data class AttachmentsCard(
    val attachments: List<String>,
    val uploads: List<Uri>
) : Card()

data class NoteCard(
    val noteToSelf: String? = null
) : Card()
