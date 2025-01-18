package com.example.eventify.presentation.ui.activities

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.example.eventify.EventifyApplication
import com.example.common.base.BaseActivity
import com.example.eventify.databinding.ActivityOnBoardingBinding
import com.example.eventify.presentation.viewmodels.OnBoardingViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class OnBoardingActivity : BaseActivity<ActivityOnBoardingBinding>(ActivityOnBoardingBinding::inflate) {

    private val onBoardingViewModel by viewModels<OnBoardingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val languageCode = onBoardingViewModel.sharedPrefLanguage.getString("language", "en") ?: "en"
        setAppLocale(this, languageCode)
        val themeMode = onBoardingViewModel.sharedPrefTheme.getInt("theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(themeMode)
    }

    override fun onCreateLight() {

        // Access the application instance
        val app = application as EventifyApplication

        // Observe global network state
        app.isNetworkConnected.observe(this) { isConnected ->
            if (!isConnected) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Connection Lost",
                    Snackbar.LENGTH_INDEFINITE
                ).apply {
                    setAction("Dismiss") { dismiss() }
                }.show()
            }
//            else (isConnected)
        }

        checkIfUserLoggedIn()
    }

    private fun checkIfUserLoggedIn(){
        val refreshToken = onBoardingViewModel.getRefreshToken()
        if(refreshToken != null){
            val condition = onBoardingViewModel.checkRefreshTokenIsValid(refreshToken)
            if(condition) {
                Intent(this,MainActivity::class.java).also {
                    startActivity(it)
                }
                finish()
            }
            else{
                onBoardingViewModel.clearTokens()
            }
        }
        else{
            onBoardingViewModel.clearTokens()
        }
    }

    private fun setAppLocale(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
}