package com.spiraclesoftware.androidsample.util

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import com.jakewharton.processphoenix.ProcessPhoenix
import com.spiraclesoftware.androidsample.util.LanguageManager.AppLanguage.CZECH
import com.spiraclesoftware.androidsample.util.LanguageManager.AppLanguage.ENGLISH
import java.util.*

class LanguageManager(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) {

    enum class AppLanguage(val code: String) {
        ENGLISH("en-US"),
        CZECH("cs-CZ");
    }

    companion object {
        private val DEFAULT_LANGUAGE = ENGLISH
        private const val APP_LANGUAGE_KEY = "appLanguage"
    }

    fun toggleLanguageAndRestart() {
        val newLanguage = when (getCurrentLanguage()) {
            ENGLISH -> CZECH
            CZECH -> ENGLISH
        }
        setLanguageAndRestart(newLanguage)
    }

    fun setLanguageAndRestart(language: AppLanguage) {
        persistLanguage(language)
        ProcessPhoenix.triggerRebirth(context)
    }

    fun getCurrentLanguage(): AppLanguage {
        val languagePref = sharedPreferences.getString(APP_LANGUAGE_KEY, DEFAULT_LANGUAGE.name)!!
        return AppLanguage.valueOf(languagePref)
    }

    private fun persistLanguage(language: AppLanguage) {
        sharedPreferences.edit()
            .putString(APP_LANGUAGE_KEY, language.name)
            .apply()
    }

    fun applyLocale(context: Context): Context {
        return updateResources(context)
    }

    private fun updateResources(context: Context): Context {
        val newLocale = Locale(getCurrentLanguage().code)
        Locale.setDefault(newLocale)

        val oldConfiguration = context.resources.configuration
        val newConfiguration = Configuration(oldConfiguration).apply {
            setLocale(newLocale)
        }
        return context.createConfigurationContext(newConfiguration)
    }
}