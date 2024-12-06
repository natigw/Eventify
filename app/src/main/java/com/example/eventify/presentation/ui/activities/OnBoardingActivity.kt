package com.example.eventify.presentation.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import com.example.eventify.common.base.BaseActivity
import com.example.eventify.databinding.ActivityOnBoardingBinding
import com.example.eventify.presentation.viewmodels.OnBoardingViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class OnBoardingActivity : BaseActivity<ActivityOnBoardingBinding>(ActivityOnBoardingBinding::inflate) {


    val onBoardingViewModel by viewModels<OnBoardingViewModel>()

    override fun onCreateLight() {
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