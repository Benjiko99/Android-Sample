package com.spiraclesoftware.androidsample

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.spiraclesoftware.androidsample.data.network.MainApi
import com.spiraclesoftware.androidsample.data.network.adapter.ConversionRatesAdapter
import com.spiraclesoftware.androidsample.data.network.adapter.MoneyAdapter
import com.spiraclesoftware.androidsample.domain.model.ConversionRates
import com.spiraclesoftware.androidsample.domain.model.Money
import com.spiraclesoftware.core.data.network.adapter.ZonedDateTimeAdapter
import com.spiraclesoftware.core.domain.UniqueIdentifier
import com.spiraclesoftware.core.domain.UniqueIdentifierAdapter
import com.spiraclesoftware.core.utils.LanguageManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import org.threeten.bp.ZonedDateTime
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    single { SampleApplication.getSharedPreferences(androidApplication()) }

    single { (get() as Retrofit).create(MainApi::class.java) }

    single {
        Retrofit.Builder()
            .client(get() as OkHttpClient)
            .baseUrl(SampleApplication.API_SERVICE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(get() as Gson))
            .build()
    }

    single<Gson> {
        GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            .registerTypeAdapter(
                UniqueIdentifier::class.java,
                UniqueIdentifierAdapter()
            )
            .registerTypeAdapter(
                ZonedDateTime::class.java,
                ZonedDateTimeAdapter()
            )
            .registerTypeAdapter(
                Money::class.java,
                MoneyAdapter()
            )
            .registerTypeAdapter(
                ConversionRates::class.java,
                ConversionRatesAdapter()
            )
            .create()
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