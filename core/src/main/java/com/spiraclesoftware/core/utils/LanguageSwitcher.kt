package com.spiraclesoftware.core.utils

import android.content.Context
import android.content.res.Configuration
import android.preference.PreferenceManager
import com.jakewharton.processphoenix.ProcessPhoenix
import com.spiraclesoftware.core.utils.LanguageSwitcher.AppLanguage.CZECH
import com.spiraclesoftware.core.utils.LanguageSwitcher.AppLanguage.ENGLISH
import java.util.*

class LanguageSwitcher {

    enum class AppLanguage(val code: String) {
        ENGLISH("en"),
        CZECH("cs");
    }

    companion object {

        val DEFAULT_LANGUAGE = ENGLISH

        private const val APP_LANGUAGE_KEY = "appLanguage"

        fun toggleLanguageAndRestart(ctx: Context) {
            val newLanguage = when (getCurrentLanguage(ctx)) {
                ENGLISH -> CZECH
                CZECH -> ENGLISH
            }
            setLanguageAndRestart(ctx, newLanguage)
        }

        fun setLanguageAndRestart(ctx: Context, language: AppLanguage) {
            persistLanguage(ctx, language)
            ProcessPhoenix.triggerRebirth(ctx)
        }

        fun getCurrentLanguage(ctx: Context): AppLanguage =
            AppLanguage.valueOf(
                PreferenceManager.getDefaultSharedPreferences(ctx)
                    .getString(APP_LANGUAGE_KEY, DEFAULT_LANGUAGE.name)!!
            )

        private fun persistLanguage(ctx: Context, language: AppLanguage) {
            PreferenceManager.getDefaultSharedPreferences(ctx).edit().apply {
                putString(APP_LANGUAGE_KEY, language.name)
                apply()
            }
        }

        fun applyLocale(ctx: Context): Context {
            persistLanguage(ctx, getCurrentLanguage(ctx))
            return updateResources(ctx, getCurrentLanguage(ctx))
        }

        private fun updateResources(ctx: Context, language: AppLanguage): Context {
            val newLocale = Locale(language.code)
            Locale.setDefault(newLocale)

            val oldConfiguration = ctx.resources.configuration
            val newConfiguration = Configuration(oldConfiguration).apply {
                setLocale(newLocale)
            }
            return ctx.createConfigurationContext(newConfiguration)
        }
    }
}