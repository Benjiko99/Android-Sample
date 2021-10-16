package com.spiraclesoftware.androidsample.feature.transaction_detail

import com.spiraclesoftware.androidsample.domain.entity.TransactionId
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.CardsFormatter
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.CardsPresenter
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val transactionDetailModule = module {

    viewModel { (id: TransactionId) ->
        TransactionDetailViewModel(id, get { parametersOf(id) })
    }

    factory { (id: TransactionId) ->
        TransactionDetailPresenter(id, get(), get(), get(), get())
    }

    single { TransactionDetailFormatter(get()) }

    single { CardsPresenter(get()) }

    single { CardsFormatter(get(), get(), get(), get()) }

}