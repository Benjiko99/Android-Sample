package com.spiraclesoftware.androidsample

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import com.facebook.stetho.Stetho
import com.jakewharton.processphoenix.ProcessPhoenix
import com.jakewharton.threetenabp.AndroidThreeTen
import com.spiraclesoftware.androidsample.data.dataModules
import com.spiraclesoftware.androidsample.ui.uiModule
import com.spiraclesoftware.core.extensions.string
import com.spiraclesoftware.core.utils.LanguageManager
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.math.MathContext


class SampleApplication : Application() {

    companion object {
        const val API_SERVICE_BASE_URL = "https://benjiko99-android-sample.builtwithdark.com/"

        /** Keep the MathContext consistent across the application */
        val mathContext = MathContext.DECIMAL32!!

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
            androidLogger()
            androidContext(this@SampleApplication)
            modules(listOf(appModule, uiModule) + dataModules)
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