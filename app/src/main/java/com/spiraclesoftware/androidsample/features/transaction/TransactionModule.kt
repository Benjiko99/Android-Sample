package com.spiraclesoftware.androidsample.features.transaction

import com.spiraclesoftware.androidsample.features.transaction.list.TransactionListViewModel
import com.spiraclesoftware.androidsample.shared.domain.TransactionsInteractor
import com.spiraclesoftware.androidsample.ui.transactiondetail.TransactionDetailPresenter
import com.spiraclesoftware.androidsample.ui.transactiondetail.TransactionDetailViewModel
import com.spiraclesoftware.androidsample.ui.transactiondetail.cards.CardsGenerator
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val transactionModule = module {

    single { CardsGenerator() }

    factory { TransactionsInteractor(get(), get()) }

    viewModel { TransactionListViewModel(get(), get(), get()) }

    viewModel { TransactionDetailViewModel(get(), get()) }
    factory { TransactionDetailPresenter(get()) }
}