package com.example.eventify.presentation.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import com.example.eventify.EventifyApplication
import com.example.common.base.BaseActivity
import com.example.eventify.databinding.ActivityOnBoardingBinding
import com.example.eventify.presentation.viewmodels.OnBoardingViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class OnBoardingActivity : BaseActivity<ActivityOnBoardingBinding>(ActivityOnBoardingBinding::inflate) {

    private val onBoardingViewModel by viewModels<OnBoardingViewModel>()

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
            if(condition){
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
}