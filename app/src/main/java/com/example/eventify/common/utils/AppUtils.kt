package com.example.eventify.common.utils

import android.content.Context
import android.content.Intent
import com.example.eventify.data.remote.interceptor.TokenManager
import com.example.eventify.presentation.ui.activities.OnBoardingActivity
import kotlinx.coroutines.channels.Channel

object AppUtils {
    fun handleLogout(context: Context, tokenProvider: TokenManager) {

        tokenProvider.clearTokens()

        val intent = Intent(context, OnBoardingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }




    val authChannel = Channel<Unit>()

}