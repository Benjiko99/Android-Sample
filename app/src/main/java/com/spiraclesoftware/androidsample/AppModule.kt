package com.spiraclesoftware.androidsample

import com.spiraclesoftware.androidsample.data_local.RoomDataSource
import com.spiraclesoftware.androidsample.data_remote.RetrofitDataSource
import com.spiraclesoftware.androidsample.domain.LocalDataSource
import com.spiraclesoftware.androidsample.domain.RemoteDataSource
import com.spiraclesoftware.androidsample.util.LanguageManager
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {

    single { androidApplication() as SampleApplication }

    single { SampleApplication.getSharedPreferences(androidContext()) }

    single<LocalDataSource> { RoomDataSource(get()) }

    single<RemoteDataSource> { RetrofitDataSource(get()) }

    single { LanguageManager(androidApplication(), get()) }

}