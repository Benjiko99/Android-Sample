package com.spiraclesoftware.androidsample.feature.category_select

import com.spiraclesoftware.androidsample.domain.entity.TransactionCategory
import com.spiraclesoftware.androidsample.domain.entity.TransactionId
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val categorySelectModule = module {

    viewModel { (id: TransactionId, initialCategory: TransactionCategory) ->
        CategorySelectViewModel(id, initialCategory, get())
    }

    factory { CategorySelectPresenter(get(), get()) }

    single { CategoryModelFormatter(get()) }

}