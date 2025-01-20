package com.example.eventify.presentation.ui.activities

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.viewModels
import com.example.common.base.BaseActivity
import com.example.eventify.EventifyApplication
import com.example.eventify.databinding.ActivityOnBoardingBinding
import com.example.eventify.presentation.viewmodels.OnBoardingViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class OnBoardingActivity : BaseActivity<ActivityOnBoardingBinding>(ActivityOnBoardingBinding::inflate) {

    private val viewModel by viewModels<OnBoardingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeLanguage(viewModel.currentLanguage!!)
    }

    override fun onCreateLight() {
        val app = application as EventifyApplication
        app.isNetworkConnected.observe(this) { isConnected ->
            if (!isConnected)
                navigateToNetworkActivity()
        }

        checkIfUserLoggedIn()

    }

    private fun navigateToNetworkActivity() {
        Intent(this@OnBoardingActivity, NetworkLostActivity::class.java).also {
            startActivity(it)
        }
    }

    private fun checkIfUserLoggedIn() {
        val refreshToken = viewModel.getRefreshToken()
        if (refreshToken != null) {
            val condition = viewModel.checkRefreshTokenIsValid(refreshToken)
            if (condition) {
                Intent(this, MainActivity::class.java).also {
                    startActivity(it)
                }
                finish()
            } else {
                viewModel.clearTokens()
            }
        } else {
            viewModel.clearTokens()
        }
    }

    private fun setAppLocale(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    private fun changeLanguage(languageCode: String) {
        setAppLocale(this, languageCode)
        viewModel.sharedPrefLanguage.edit().putString("language", languageCode).apply()
    }
}