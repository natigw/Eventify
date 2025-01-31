package com.example.eventify.presentation.ui.activities

import android.content.Intent
import androidx.activity.viewModels
import com.example.common.base.BaseActivity
import com.example.eventify.EventifyApplication
import com.example.eventify.databinding.ActivityOnBoardingBinding
import com.example.eventify.presentation.viewmodels.OnBoardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingActivity : BaseActivity<ActivityOnBoardingBinding>(ActivityOnBoardingBinding::inflate) {

    private val viewModel by viewModels<OnBoardingViewModel>()

    override fun onCreateLight() {
        val app = application as EventifyApplication
        app.isNetworkConnected.observe(this) { isConnected ->
            if (!isConnected)
                navigateToNetworkActivity()
        }

        checkIfUserLoggedIn()
    }

    private fun checkIfUserLoggedIn() {
        val tokenValidity = viewModel.checkUser()
        if(tokenValidity!=null){
            Intent(this,MainActivity::class.java).also {
                startActivity(it)
            }
            finish()
        }
    }

    private fun navigateToNetworkActivity() {
        Intent(this@OnBoardingActivity, NetworkLostActivity::class.java).also {
            startActivity(it)
        }
    }
}