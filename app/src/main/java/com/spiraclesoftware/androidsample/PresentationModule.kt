package com.spiraclesoftware.androidsample

import com.spiraclesoftware.androidsample.category_select.CategorySelectPresenter
import com.spiraclesoftware.androidsample.category_select.CategorySelectViewModel
import com.spiraclesoftware.androidsample.domain.model.TransactionCategory
import com.spiraclesoftware.androidsample.domain.model.TransactionId
import com.spiraclesoftware.androidsample.shared.ExceptionFormatter
import com.spiraclesoftware.androidsample.text_input.TextInputType
import com.spiraclesoftware.androidsample.text_input.TextInputViewModel
import com.spiraclesoftware.androidsample.transaction_detail.TransactionDetailPresenter
import com.spiraclesoftware.androidsample.transaction_detail.TransactionDetailViewModel
import com.spiraclesoftware.androidsample.transaction_detail.cards.CardsPresenter
import com.spiraclesoftware.androidsample.transaction_list.TransactionListPresenter
import com.spiraclesoftware.androidsample.transaction_list.TransactionListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    single { ExceptionFormatter(androidContext()) }

    viewModel { TransactionListViewModel(get()) }
    factory { TransactionListPresenter(get(), get(), get(), get()) }

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