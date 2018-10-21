package com.spiraclesoftware.airbankinterview.di

import android.content.Context
import com.spiraclesoftware.airbankinterview.AirBankApplication
import dagger.Module
import dagger.Provides

/**
 * Defines all the classes that need to be provided in the scope of the app.
 *
 * Define here all objects that are shared throughout the app, like SharedPreferences, navigators or
 * others. If some of those objects are singletons, they should be annotated with `@Singleton`.
 */
@Module
class AppModule {

    @Provides
    fun provideContext(application: AirBankApplication): Context {
        return application.applicationContext
    }
}