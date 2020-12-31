package com.spiraclesoftware.androidsample.ui

import com.spiraclesoftware.androidsample.domain.model.TransactionCategory
import com.spiraclesoftware.androidsample.domain.model.TransactionId
import com.spiraclesoftware.androidsample.ui.categoryselect.CategorySelectPresenter
import com.spiraclesoftware.androidsample.ui.categoryselect.CategorySelectViewModel
import com.spiraclesoftware.androidsample.ui.textinput.TextInputType
import com.spiraclesoftware.androidsample.ui.textinput.TextInputViewModel
import com.spiraclesoftware.androidsample.ui.transactiondetail.TransactionDetailPresenter
import com.spiraclesoftware.androidsample.ui.transactiondetail.TransactionDetailViewModel
import com.spiraclesoftware.androidsample.ui.transactiondetail.cards.CardsPresenter
import com.spiraclesoftware.androidsample.ui.transactionlist.TransactionListPresenter
import com.spiraclesoftware.androidsample.ui.transactionlist.TransactionListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {

    viewModel { TransactionListViewModel(get()) }
    factory { TransactionListPresenter(get(), get(), get()) }

    viewModel { (id: TransactionId) -> TransactionDetailViewModel(id, get(), get()) }
    factory { TransactionDetailPresenter(get()) }
    single { CardsPresenter() }

    viewModel { (inputType: TextInputType, requestKey: String, initialValue: String) ->
        TextInputViewModel(inputType, requestKey, initialValue)
    }

    viewModel { (id: TransactionId, initialCategory: TransactionCategory) ->
        CategorySelectViewModel(id, initialCategory, get())
    }
    factory { CategorySelectPresenter(get()) }
}