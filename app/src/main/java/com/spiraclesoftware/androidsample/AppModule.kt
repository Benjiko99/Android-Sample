package com.spiraclesoftware.androidsample

import com.spiraclesoftware.androidsample.data.DiskDataSourceImpl
import com.spiraclesoftware.androidsample.data.MemoryDataSourceImpl
import com.spiraclesoftware.androidsample.data.NetworkDataSourceImpl
import com.spiraclesoftware.androidsample.domain.DiskDataSource
import com.spiraclesoftware.androidsample.domain.MemoryDataSource
import com.spiraclesoftware.androidsample.domain.NetworkDataSource
import com.spiraclesoftware.core.utils.LanguageManager
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {

    single { androidApplication() as SampleApplication }

    single { SampleApplication.getSharedPreferences(androidContext()) }

    single { LanguageManager(androidApplication(), get()) }

    single<MemoryDataSource> { MemoryDataSourceImpl() }

    single<DiskDataSource> { DiskDataSourceImpl(get()) }

    single<NetworkDataSource> { NetworkDataSourceImpl(get()) }

}