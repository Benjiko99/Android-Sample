package com.spiraclesoftware.androidsample

import android.content.Context
import com.spiraclesoftware.androidsample.data_local.RoomDataSource
import com.spiraclesoftware.androidsample.data_remote.RetrofitDataSource
import com.spiraclesoftware.androidsample.domain.LocalDataSource
import com.spiraclesoftware.androidsample.domain.RemoteDataSource
import com.spiraclesoftware.androidsample.framework.extensions.string
import com.spiraclesoftware.androidsample.framework.utils.ThemeManager
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {

    single {
        val ctx = androidApplication()
        val key = ctx.string(R.string.shared_preferences_key)
        ctx.getSharedPreferences(key, Context.MODE_PRIVATE)
    }

    single<LocalDataSource> { RoomDataSource(get()) }

    single<RemoteDataSource> { RetrofitDataSource(get()) }

    single { ThemeManager(get()) }

}