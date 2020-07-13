package com.spiraclesoftware.androidsample.ui.transactionlist

import com.spiraclesoftware.androidsample.domain.model.Account
import com.spiraclesoftware.androidsample.domain.model.ConversionRates
import com.spiraclesoftware.androidsample.domain.model.Transaction

sealed class TransactionListViewState

object Loading : TransactionListViewState()

data class ListReady(
    val account: Account,
    val transactions: List<Transaction>,
    val conversionRates: ConversionRates
) : TransactionListViewState()

object NetworkError : TransactionListViewState()
