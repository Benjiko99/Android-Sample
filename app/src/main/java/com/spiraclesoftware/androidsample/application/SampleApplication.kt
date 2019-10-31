package com.spiraclesoftware.androidsample.application

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.facebook.stetho.Stetho
import com.jakewharton.processphoenix.ProcessPhoenix
import com.spiraclesoftware.androidsample.application.di.appModule
import com.spiraclesoftware.androidsample.application.di.featureModules
import com.spiraclesoftware.androidsample.application.di.sharedModule
import com.spiraclesoftware.core.di.coreModule
import com.spiraclesoftware.core.utils.LanguageSwitcher
import com.squareup.leakcanary.LeakCanary
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class SampleApplication : Application() {

    companion object {
        const val API_SERVICE_BASE_URL = "https://my-json-server.typicode.com/Benjiko99/Android-Sample/"
    }

    override fun onCreate() {
        super.onCreate()

        if (ProcessPhoenix.isPhoenixProcess(this)) return
        if (LeakCanary.isInAnalyzerProcess(this)) return

        LeakCanary.install(this)
        Stetho.initializeWithDefaults(this)

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