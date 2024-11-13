package com.example.eventify.presentation.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.eventify.common.base.BaseActivity
import com.example.eventify.databinding.ActivityOnBoardingBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class OnBoardingActivity : BaseActivity<ActivityOnBoardingBinding>(ActivityOnBoardingBinding::inflate) {

    @Inject
    @Named("UserLoggedIn")
    lateinit var sharedPrefLoggedIn: SharedPreferences

    override fun onCreateLight() {
        if (sharedPrefLoggedIn.getBoolean("login_status", false)) {
            Intent(this, MainActivity::class.java).apply {
                startActivity(this)
                finish()
            }
        }
    }
}