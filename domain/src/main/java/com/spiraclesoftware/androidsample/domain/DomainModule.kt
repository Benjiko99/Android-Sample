package com.spiraclesoftware.androidsample.domain

import com.spiraclesoftware.androidsample.domain.interactor.AccountsInteractor
import com.spiraclesoftware.androidsample.domain.interactor.ConversionRatesInteractor
import com.spiraclesoftware.androidsample.domain.interactor.ProfileInteractor
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import com.spiraclesoftware.androidsample.domain.service.CurrencyConverter
import org.koin.dsl.module

private val interactorModule = module {

    factory { ProfileInteractor(get(), get()) }

    factory { AccountsInteractor(get(), get()) }

    factory { TransactionsInteractor(get(), get()) }

    factory { ConversionRatesInteractor(get(), get()) }

}

private val serviceModule = module {

    factory { CurrencyConverter(get()) }

}

val domainModule = interactorModule + serviceModule
