package com.spiraclesoftware.androidsample.data

import com.spiraclesoftware.androidsample.data.disk.DiskDataSource
import com.spiraclesoftware.androidsample.data.network.NetworkDataSource
import com.spiraclesoftware.androidsample.domain.interactor.AccountsInteractor
import com.spiraclesoftware.androidsample.domain.interactor.ConversionRatesInteractor
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import org.koin.dsl.module

val dataModule = module {

    single { DiskDataSource() }

    single { NetworkDataSource(get()) }

    factory { AccountsInteractor(get()) }

    factory {
        TransactionsInteractor(
            get(),
            get()
        )
    }

    factory {
        ConversionRatesInteractor(
            get(),
            get()
        )
    }
}