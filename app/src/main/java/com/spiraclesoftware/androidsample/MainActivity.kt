package com.spiraclesoftware.androidsample

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.spiraclesoftware.core.utils.LanguageManager
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        val languageManager: LanguageManager by inject()
        super.attachBaseContext(languageManager.applyLocale(newBase))
    }

    private val navController by lazy { findNavController(R.id.navHostFragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main__activity)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

}
