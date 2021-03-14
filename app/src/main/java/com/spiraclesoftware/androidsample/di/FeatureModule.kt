package com.spiraclesoftware.androidsample.di

import com.spiraclesoftware.androidsample.domain.entity.TransactionCategory
import com.spiraclesoftware.androidsample.domain.entity.TransactionId
import com.spiraclesoftware.androidsample.feature.category_select.CategoryModelFormatter
import com.spiraclesoftware.androidsample.feature.category_select.CategorySelectPresenter
import com.spiraclesoftware.androidsample.feature.category_select.CategorySelectViewModel
import com.spiraclesoftware.androidsample.feature.text_input.TextInputType
import com.spiraclesoftware.androidsample.feature.text_input.TextInputViewModel
import com.spiraclesoftware.androidsample.feature.transaction_detail.TransactionDetailFormatter
import com.spiraclesoftware.androidsample.feature.transaction_detail.TransactionDetailPresenter
import com.spiraclesoftware.androidsample.feature.transaction_detail.TransactionDetailViewModel
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.CardsFormatter
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.CardsPresenter
import com.spiraclesoftware.androidsample.feature.transaction_list.TransactionListFormatter
import com.spiraclesoftware.androidsample.feature.transaction_list.TransactionListPresenter
import com.spiraclesoftware.androidsample.feature.transaction_list.TransactionListViewModel
import com.spiraclesoftware.androidsample.formatter.*
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val featureModule = module {

    single { ExceptionFormatter(androidContext()) }
    single { MoneyFormatter() }
    single { TransactionStatusFormatter() }
    single { TransactionStatusCodeFormatter() }
    single { TransactionCategoryFormatter() }
    single { TransferDirectionFilterFormatter() }

    viewModel { TransactionListViewModel(get()) }
    factory { TransactionListPresenter(get(), get(), get(), get(), get()) }
    single { TransactionListFormatter() }

    viewModel { (id: TransactionId) ->
        TransactionDetailViewModel(id, get { parametersOf(id) })
    }
    factory { (id: TransactionId) ->
        TransactionDetailPresenter(id, get(), get(), get(), get(), get())
    }
    single { TransactionDetailFormatter() }
    single { CardsPresenter() }
    single { CardsFormatter() }

    viewModel { (inputType: TextInputType, requestKey: String, initialValue: String) ->
        TextInputViewModel(inputType, requestKey, initialValue)
    }

    viewModel { (id: TransactionId, initialCategory: TransactionCategory) ->
        CategorySelectViewModel(id, initialCategory, get())
    }
    factory { CategorySelectPresenter(get(), get()) }
    single { CategoryModelFormatter() }

}