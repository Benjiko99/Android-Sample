package com.spiraclesoftware.androidsample.application.di

import com.spiraclesoftware.androidsample.shared.data.disk.DiskDataSource
import com.spiraclesoftware.androidsample.shared.data.network.NetworkDataSource
import com.spiraclesoftware.androidsample.shared.domain.AccountsInteractor
import com.spiraclesoftware.androidsample.shared.domain.ConversionRatesInteractor
import com.spiraclesoftware.androidsample.shared.domain.TransactionsInteractor
import org.koin.dsl.module

val sharedModule = module {

    single { DiskDataSource() }

    single { NetworkDataSource(get()) }

    factory { AccountsInteractor(get()) }

    factory { TransactionsInteractor(get(), get()) }

    factory { ConversionRatesInteractor(get(), get()) }
}