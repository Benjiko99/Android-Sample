package com.spiraclesoftware.androidsample.ui.transactiondetail

import com.spiraclesoftware.androidsample.domain.model.TransactionCategory
import org.threeten.bp.ZonedDateTime

sealed class TransactionDetailViewState

object Loading : TransactionDetailViewState()

data class DetailReady(
    val name: String,
    val processingDate: ZonedDateTime,
    val formattedMoney: String,
    val contributesToBalance: Boolean,
    val isSuccessful: Boolean,
    val category: TransactionCategory,
    val cardItems: List<CardItem>
) : TransactionDetailViewState()
