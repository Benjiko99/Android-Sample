package com.spiraclesoftware.androidsample

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import com.facebook.stetho.Stetho
import com.jakewharton.processphoenix.ProcessPhoenix
import com.jakewharton.threetenabp.AndroidThreeTen
import com.spiraclesoftware.androidsample.data.dataModule
import com.spiraclesoftware.androidsample.domain.domainModule
import com.spiraclesoftware.androidsample.extensions.string
import com.spiraclesoftware.androidsample.modules.appModule
import com.spiraclesoftware.androidsample.modules.presentationModule
import com.spiraclesoftware.androidsample.utils.LanguageManager
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber
import timber.log.Timber.DebugTree

class SampleApplication : Application() {

    companion object {
        fun getSharedPreferences(ctx: Context): SharedPreferences {
            val key = ctx.string(R.string.shared_preferences_key)
            return ctx.getSharedPreferences(key, Context.MODE_PRIVATE)
        }
    }

    override fun onCreate() {
        super.onCreate()

        if (ProcessPhoenix.isPhoenixProcess(this)) return

        Stetho.initializeWithDefaults(this)
        AndroidThreeTen.init(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@SampleApplication)
            modules(appModule + presentationModule + domainModule + dataModule)
        }
    }

    /** Executes before onCreate(), thus we cannot inject dependencies with Koin */
    override fun attachBaseContext(base: Context) {
        val sharedPreferences = getSharedPreferences(base)
        val languageManager = LanguageManager(base, sharedPreferences)
        super.attachBaseContext(languageManager.applyLocale(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        get<LanguageManager>().applyLocale(this)
    }

}