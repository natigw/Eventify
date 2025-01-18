package com.example.eventify.presentation.ui.activities

import android.content.Intent
import android.graphics.Color
import android.view.View
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.common.base.BaseActivity
import com.example.common.utils.nancyToastError
import com.example.eventify.EventifyApplication
import com.example.eventify.R
import com.example.eventify.databinding.ActivityNetworkLostBinding
import com.google.android.material.snackbar.Snackbar
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
        buttonListeners()

    }

    private fun buttonListeners() {
        binding.buttonRetryConnectionLost.setOnClickListener {
            lifecycleScope.launch {
                blockRetryButton()
                delay(2000)
                nancyToastError(applicationContext, getString(R.string.connection_lost_retry))
                resetRetryButton()
            }
        }

        this.onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                
            }
        })
    }

    private fun blockRetryButton() {
        binding.progressBarRetryConnectionLost.visibility = View.VISIBLE
        binding.buttonRetryConnectionLost.apply {
            isEnabled = false
            text = null
            setBackgroundColor(applicationContext.getColor(R.color.button_disabled))
        }
    }

    private fun resetRetryButton() {
        binding.progressBarRetryConnectionLost.visibility = View.INVISIBLE
        binding.buttonRetryConnectionLost.apply {
            isEnabled = true
            text = getString(R.string.retry)
            setBackgroundColor(applicationContext.getColor(R.color.green_light_eventify))
        }
    }


}