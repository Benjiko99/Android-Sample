package com.spiraclesoftware.androidsample.framework.utils

import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate.*

class ThemeManager(
    private val sharedPreferences: SharedPreferences
) {

    enum class AppTheme() {
        DAY, NIGHT, FOLLOW_SYSTEM;
    }

    companion object {
        private val DEFAULT_THEME = AppTheme.FOLLOW_SYSTEM
        private const val APP_THEME_KEY = "appTheme"
    }

    fun applyCurrentTheme() {
        applyTheme(getCurrentTheme())
    }

    private fun applyTheme(appTheme: AppTheme) {
        val result = when (appTheme) {
            AppTheme.FOLLOW_SYSTEM -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    MODE_NIGHT_FOLLOW_SYSTEM
                } else {
                    MODE_NIGHT_AUTO_BATTERY
                }
            }
            AppTheme.DAY -> {
                MODE_NIGHT_NO
            }
            AppTheme.NIGHT -> {
                MODE_NIGHT_YES
            }
        }

        setDefaultNightMode(result)
    }

    fun getCurrentTheme(): AppTheme {
        val themePref = sharedPreferences.getString(APP_THEME_KEY, DEFAULT_THEME.name)!!
        return AppTheme.valueOf(themePref)
    }

    private fun persistTheme(theme: AppTheme) {
        sharedPreferences.edit()
            .putString(APP_THEME_KEY, theme.name)
            .apply()
    }
}