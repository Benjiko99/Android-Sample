package com.spiraclesoftware.androidsample.application.di

import com.spiraclesoftware.androidsample.shared.data.ConversionRatesRepository
import com.spiraclesoftware.androidsample.shared.data.TransactionsRepository
import com.spiraclesoftware.core.data.AssociatedItemCache
import com.spiraclesoftware.core.data.AssociatedListCache
import org.koin.dsl.module

val sharedModule = module {

    single { TransactionsRepository(get(), get(), AssociatedListCache()) }

    single { ConversionRatesRepository(get(), get(), AssociatedItemCache()) }
}