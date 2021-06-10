package com.spiraclesoftware.androidsample.feature.profile

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val profileModule = module {

    viewModel { ProfileViewModel(get()) }

    factory { ProfilePresenter(get(), get(), get()) }

    single { ProfileFormatter(get()) }

}