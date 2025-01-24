package com.example.eventify.presentation.ui.activities

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import com.example.common.base.BaseActivity
import com.example.common.utils.blockButton
import com.example.common.utils.nancyToastError
import com.example.common.utils.resetButton
import com.example.eventify.EventifyApplication
import com.example.eventify.R
import com.example.eventify.databinding.ActivityNetworkLostBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NetworkLostActivity : BaseActivity<ActivityNetworkLostBinding>(ActivityNetworkLostBinding::inflate) {

    override fun onCreateLight() {
        val app = application as EventifyApplication

        app.isNetworkConnected.observe(this) { isConnected ->
            if (isConnected)
                finish()
        }

        this.onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        })

        buttonListeners()
    }

    private fun buttonListeners() {
        binding.buttonRetryConnectionLost.setOnClickListener {
            lifecycleScope.launch {
                blockButton(
                    progressBar = binding.progressBarRetryConnectionLost,
                    button = binding.buttonRetryConnectionLost
                )
                delay(2000)
                nancyToastError(applicationContext, getString(R.string.connection_lost_retry))
                resetButton(
                    progressBar = binding.progressBarRetryConnectionLost,
                    button = binding.buttonRetryConnectionLost,
                    buttonText = getString(R.string.retry),
                    buttonColor = applicationContext.getColor(R.color.green_light_eventify)
                )
            }
        }
    }
}