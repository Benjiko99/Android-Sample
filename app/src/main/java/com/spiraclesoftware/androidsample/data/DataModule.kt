package com.spiraclesoftware.androidsample.data

import androidx.room.Room
import com.spiraclesoftware.androidsample.data.disk.DiskDataSource
import com.spiraclesoftware.androidsample.data.disk.MainDatabase
import com.spiraclesoftware.androidsample.data.disk.MainDatabase.Migrations
import com.spiraclesoftware.androidsample.data.memory.MemoryDataSource
import com.spiraclesoftware.androidsample.data.network.NetworkDataSource
import com.spiraclesoftware.androidsample.domain.policy.CurrencyConverter
import com.spiraclesoftware.androidsample.domain.policy.TransactionsPolicy
import com.spiraclesoftware.androidsample.domain.interactor.AccountsInteractor
import com.spiraclesoftware.androidsample.domain.interactor.ConversionRatesInteractor
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val databaseModule = module {

    single {
        Room.databaseBuilder(androidContext(), MainDatabase::class.java, "main.db")
            .fallbackToDestructiveMigration()
            .addMigrations(Migrations.MIGRATION_EXAMPLE)
            .build()
    }

    single { get<MainDatabase>().transactionsDao() }

}

private val interactorModule = module {

    factory { AccountsInteractor(get()) }

    factory { TransactionsInteractor(get(), get()) }

    factory { ConversionRatesInteractor(get(), get()) }

}

private val serviceModule = module {

    factory { CurrencyConverter(get()) }

    factory { TransactionsPolicy(get()) }

}

val dataModules = databaseModule + interactorModule + serviceModule + module {

    single { MemoryDataSource() }

    single { DiskDataSource(get()) }

    single { NetworkDataSource(get()) }

}
