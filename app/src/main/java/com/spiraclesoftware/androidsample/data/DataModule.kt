package com.spiraclesoftware.androidsample.data

import androidx.room.Room
import com.spiraclesoftware.androidsample.data.disk.DiskDataSource
import com.spiraclesoftware.androidsample.data.disk.MainDatabase
import com.spiraclesoftware.androidsample.data.memory.MemoryDataSource
import com.spiraclesoftware.androidsample.data.network.NetworkDataSource
import com.spiraclesoftware.androidsample.domain.interactor.AccountsInteractor
import com.spiraclesoftware.androidsample.domain.interactor.ConversionRatesInteractor
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val databaseModule = module {

    single {
        Room.databaseBuilder(androidContext(), MainDatabase::class.java, "main.db")
            .build()
    }

    single { get<MainDatabase>().transactionsDao() }

}

private val interactorModule = module {

    factory { AccountsInteractor(get()) }

    factory { TransactionsInteractor(get(), get()) }

    factory { ConversionRatesInteractor(get(), get()) }

}

val dataModules = databaseModule + interactorModule + module {

    single { MemoryDataSource() }

    single { DiskDataSource(get()) }

    single { NetworkDataSource(get()) }

}
