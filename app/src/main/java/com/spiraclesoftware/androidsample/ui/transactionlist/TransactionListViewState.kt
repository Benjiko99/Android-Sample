package com.spiraclesoftware.androidsample.ui.transactionlist

import com.spiraclesoftware.androidsample.shared.domain.Account
import com.spiraclesoftware.androidsample.shared.domain.ConversionRates
import com.spiraclesoftware.androidsample.shared.domain.Transaction

sealed class TransactionListViewState

object Loading : TransactionListViewState()

data class ListReady(
    val account: Account,
    val transactions: List<Transaction>,
    val conversionRates: ConversionRates
) : TransactionListViewState()

object NetworkError : TransactionListViewState()
