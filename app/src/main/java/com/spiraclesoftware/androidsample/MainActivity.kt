package com.spiraclesoftware.androidsample

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.spiraclesoftware.androidsample.framework.utils.LanguageManager
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        val languageManager: LanguageManager by inject()
        super.attachBaseContext(languageManager.applyLocale(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }

    override fun onSupportNavigateUp() = findNavController(R.id.navHostFragment).navigateUp()

}
