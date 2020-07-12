package com.spiraclesoftware.androidsample.application.di

import com.spiraclesoftware.androidsample.shared.data.AccountRepository
import com.spiraclesoftware.androidsample.shared.data.ConversionRatesRepository
import com.spiraclesoftware.androidsample.shared.data.TransactionsRepository
import com.spiraclesoftware.androidsample.shared.data.disk.DiskDataSource
import com.spiraclesoftware.androidsample.shared.data.network.NetworkDataSource
import com.spiraclesoftware.core.data.AssociatedItemCache
import com.spiraclesoftware.core.data.AssociatedListCache
import org.koin.dsl.module

val sharedModule = module {

    single { DiskDataSource(AssociatedListCache()) }

    single { NetworkDataSource(get()) }

    single { AccountRepository() }

    single { TransactionsRepository(get(), get(), AssociatedListCache()) }

    single { ConversionRatesRepository(get(), get(), AssociatedItemCache()) }
}