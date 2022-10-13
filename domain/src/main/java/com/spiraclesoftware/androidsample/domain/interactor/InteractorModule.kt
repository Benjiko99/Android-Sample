package com.spiraclesoftware.androidsample.domain.interactor

import org.koin.dsl.module

val interactorModule = module {

    factory { ProfileInteractor(get(), get(), get()) }

    factory { AccountsInteractor(get(), get()) }

    factory { TransactionsInteractor(get(), get()) }

    factory { ConversionRatesInteractor(get(), get()) }

}
