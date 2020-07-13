package com.spiraclesoftware.androidsample.data.network.model

import com.spiraclesoftware.androidsample.domain.model.Transaction

data class TransactionsResponseWrapper(
    val items: List<Transaction>
)
