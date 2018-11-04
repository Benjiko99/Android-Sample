package com.spiraclesoftware.airbankinterview

import com.facebook.stetho.Stetho
import com.spiraclesoftware.airbankinterview.di.DaggerAppComponent
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class AirBankApplication : DaggerApplication() {

    companion object {
        const val API_SERVICE_BASE_URL = "https://demo0569565.mockable.io/"
    }

    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)
        // Normal app init code...

        Stetho.initializeWithDefaults(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }
}