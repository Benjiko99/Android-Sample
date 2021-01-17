package com.spiraclesoftware.androidsample.modules

import com.spiraclesoftware.androidsample.domain.model.TransactionCategory
import com.spiraclesoftware.androidsample.domain.model.TransactionId
import com.spiraclesoftware.androidsample.features.category_select.CategorySelectPresenter
import com.spiraclesoftware.androidsample.features.category_select.CategorySelectViewModel
import com.spiraclesoftware.androidsample.features.text_input.TextInputType
import com.spiraclesoftware.androidsample.features.text_input.TextInputViewModel
import com.spiraclesoftware.androidsample.features.transaction_detail.TransactionDetailPresenter
import com.spiraclesoftware.androidsample.features.transaction_detail.TransactionDetailViewModel
import com.spiraclesoftware.androidsample.features.transaction_detail.cards.CardsPresenter
import com.spiraclesoftware.androidsample.features.transaction_list.TransactionListPresenter
import com.spiraclesoftware.androidsample.features.transaction_list.TransactionListViewModel
import com.spiraclesoftware.androidsample.formatters.ExceptionFormatter
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