package com.spiraclesoftware.androidsample.features.transaction

import com.spiraclesoftware.androidsample.features.transaction.detail.TransactionDetailViewModel
import com.spiraclesoftware.androidsample.features.transaction.list.TransactionListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val transactionModule = module {

    viewModel { TransactionListViewModel(get()) }

    viewModel { TransactionDetailViewModel(get()) }
}