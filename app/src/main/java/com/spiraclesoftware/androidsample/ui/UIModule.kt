package com.spiraclesoftware.androidsample.ui

import com.spiraclesoftware.androidsample.ui.transactiondetail.TransactionDetailPresenter
import com.spiraclesoftware.androidsample.ui.transactiondetail.TransactionDetailViewModel
import com.spiraclesoftware.androidsample.ui.transactiondetail.cards.CardsGenerator
import com.spiraclesoftware.androidsample.ui.transactionlist.TransactionListPresenter
import com.spiraclesoftware.androidsample.ui.transactionlist.TransactionListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {

    viewModel { TransactionListViewModel(get()) }
    factory { TransactionListPresenter(get(), get(), get()) }

    viewModel { TransactionDetailViewModel(get()) }
    factory { TransactionDetailPresenter(get(), get()) }
    single { CardsGenerator() }
}