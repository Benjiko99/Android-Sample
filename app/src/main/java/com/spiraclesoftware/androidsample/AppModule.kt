package com.spiraclesoftware.androidsample

import com.spiraclesoftware.androidsample.data.network.MainApi
import com.spiraclesoftware.androidsample.data.network.adapter.*
import com.spiraclesoftware.core.utils.LanguageManager
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@OptIn(ExperimentalStdlibApi::class)
val appModule = module {

    single { androidApplication() as SampleApplication }

    single { SampleApplication.getSharedPreferences(androidContext()) }

    single { get<Retrofit>().create(MainApi::class.java) }

    single {
        Retrofit.Builder()
            .client(get() as OkHttpClient)
            .baseUrl(SampleApplication.API_SERVICE_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(get() as Moshi))
            .build()
    }

    single<Moshi> {
        Moshi.Builder()
            .add(BigDecimalAdapter())
            .add(CurrencyAdapter())
            .add(HttpUrlAdapter())
            .add(ZonedDateTimeAdapter())
            .add(UniqueIdentifierAdapter())
            .add(ConversionRateListAdapter())
            .build()
    }

    single {
        OkHttpClient.Builder().apply {
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

    single { LanguageManager(androidApplication(), get()) }

}