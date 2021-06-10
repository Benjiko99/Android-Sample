package com.spiraclesoftware.androidsample.feature.transaction_list

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val transactionListModule = module {

    viewModel { TransactionListViewModel(get()) }

    factory { TransactionListPresenter(get(), get(), get(), get(), get()) }

    single { TransactionListFormatter() }

}