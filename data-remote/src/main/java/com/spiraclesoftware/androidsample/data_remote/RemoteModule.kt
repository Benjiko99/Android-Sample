package com.spiraclesoftware.androidsample.data_remote

import com.spiraclesoftware.androidsample.data_remote.adapter.BigDecimalAdapter
import com.spiraclesoftware.androidsample.data_remote.adapter.CurrencyAdapter
import com.spiraclesoftware.androidsample.data_remote.adapter.ZonedDateTimeAdapter
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit.SECONDS

val remoteModule = module {

    single { get<Retrofit>().create(MainApi::class.java) }

    single {
        Retrofit.Builder()
            .client(get() as OkHttpClient)
            .baseUrl("https://sleepy-hamlet-89904.herokuapp.com/api/")
            .addConverterFactory(MoshiConverterFactory.create(get() as Moshi))
            .build()
    }

    single<Moshi> {
        Moshi.Builder()
            .add(BigDecimalAdapter())
            .add(CurrencyAdapter())
            .add(ZonedDateTimeAdapter())
            .build()
    }

    single {
        OkHttpClient.Builder().apply {
            val timeout = 30L
            connectTimeout(timeout, SECONDS)
            callTimeout(timeout, SECONDS)
            readTimeout(timeout, SECONDS)
            writeTimeout(timeout, SECONDS)

            if (BuildConfig.DEBUG) {
                addInterceptor(get() as HttpLoggingInterceptor)
            }
        }.build()
    }

    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

}