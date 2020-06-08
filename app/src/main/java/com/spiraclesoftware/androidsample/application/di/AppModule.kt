package com.spiraclesoftware.androidsample.application.di

import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.spiraclesoftware.androidsample.BuildConfig
import com.spiraclesoftware.androidsample.application.SampleApplication
import com.spiraclesoftware.androidsample.application.data.ApiService
import com.spiraclesoftware.core.data.LiveDataCallAdapterFactory
import com.spiraclesoftware.core.data.UniqueIdentifier
import com.spiraclesoftware.core.data.UniqueIdentifierAdapter
import com.spiraclesoftware.core.data.ZonedDateTimeAdapter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import org.threeten.bp.ZonedDateTime
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    single {
        androidApplication().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

    single<ApiService> { (get() as Retrofit).create(ApiService::class.java) }

    single<Retrofit> {
        Retrofit.Builder()
            .client(get() as OkHttpClient)
            .baseUrl(SampleApplication.API_SERVICE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(get() as Gson))
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
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
            .create()
    }

    single<OkHttpClient> {
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
}