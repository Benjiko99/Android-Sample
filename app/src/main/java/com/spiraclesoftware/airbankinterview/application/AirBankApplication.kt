package com.spiraclesoftware.airbankinterview.application

import android.content.Context
import android.content.res.Configuration
import com.facebook.stetho.Stetho
import com.jakewharton.processphoenix.ProcessPhoenix
import com.spiraclesoftware.airbankinterview.application.di.DaggerAppComponent
import com.spiraclesoftware.core.utils.LanguageSwitcher
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class AirBankApplication : DaggerApplication() {

    companion object {
        const val API_SERVICE_BASE_URL = "https://demo0569565.mockable.io/"
    }

    override fun onCreate() {
        super.onCreate()

        if (ProcessPhoenix.isPhoenixProcess(this)) return
        if (LeakCanary.isInAnalyzerProcess(this)) return

        LeakCanary.install(this)
        Stetho.initializeWithDefaults(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LanguageSwitcher.applyLocale(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LanguageSwitcher.applyLocale(this)
    }
}