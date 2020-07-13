package com.spiraclesoftware.androidsample.features.transaction

import com.spiraclesoftware.androidsample.ui.transactiondetail.TransactionDetailPresenter
import com.spiraclesoftware.androidsample.ui.transactiondetail.TransactionDetailViewModel
import com.spiraclesoftware.androidsample.ui.transactiondetail.cards.CardsGenerator
import com.spiraclesoftware.androidsample.ui.transactionlist.TransactionListPresenter
import com.spiraclesoftware.androidsample.ui.transactionlist.TransactionListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val transactionModule = module {

    single { CardsGenerator() }

    viewModel { TransactionListViewModel(get()) }
    factory { TransactionListPresenter(get(), get(), get()) }

    viewModel { TransactionDetailViewModel(get(), get()) }
    factory { TransactionDetailPresenter(get()) }
}