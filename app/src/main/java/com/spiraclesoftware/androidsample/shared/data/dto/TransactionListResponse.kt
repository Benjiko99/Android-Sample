package com.spiraclesoftware.androidsample.shared.data.dto

import com.spiraclesoftware.androidsample.shared.domain.Transaction

data class TransactionListResponse(
    val items: List<Transaction>
)
