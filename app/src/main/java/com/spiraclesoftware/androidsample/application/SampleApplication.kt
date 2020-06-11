package com.spiraclesoftware.androidsample.application

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.facebook.stetho.Stetho
import com.jakewharton.processphoenix.ProcessPhoenix
import com.jakewharton.threetenabp.AndroidThreeTen
import com.spiraclesoftware.androidsample.application.di.appModule
import com.spiraclesoftware.androidsample.application.di.featureModules
import com.spiraclesoftware.androidsample.application.di.sharedModule
import com.spiraclesoftware.core.di.coreModule
import com.spiraclesoftware.core.utils.LanguageSwitcher
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class SampleApplication : Application() {

    companion object {
        const val API_SERVICE_BASE_URL = "https://benjiko99-android-sample.builtwithdark.com/"
    }

    override fun onCreate() {
        super.onCreate()

        if (ProcessPhoenix.isPhoenixProcess(this)) return

        Stetho.initializeWithDefaults(this)
        AndroidThreeTen.init(this)

        startKoin {
            androidLogger()
            androidContext(this@SampleApplication)
            modules(listOf(coreModule, appModule, sharedModule) + featureModules)
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LanguageSwitcher.applyLocale(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LanguageSwitcher.applyLocale(this)
    }
}