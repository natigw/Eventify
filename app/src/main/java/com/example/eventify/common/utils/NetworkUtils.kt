package com.example.eventify.common.utils

import android.content.Context
import android.content.Intent
import com.example.eventify.data.remote.interceptor.TokenManager
import com.example.eventify.presentation.ui.activities.OnBoardingActivity

object NetworkUtils {
    lateinit var tokenManager: TokenManager

    suspend fun <T>handleResponse(request : suspend ()->retrofit2.Response<T>) : T{
        try {
            val response = request()

            if(response.isSuccessful && response.body() != null){
                return response.body()!!
            }
            else{
                throw Exception("Error ${response.code()}: ${response.errorBody()?.string()}")
            }
        }
        catch (e : Exception){
            throw e
        }
    }


    fun initializeTokenManager(tokenManager: TokenManager){
        this.tokenManager = tokenManager
    }


    suspend fun handleInvalidAccessToken(){
        val newAccessToken = tokenManager.refreshToken()
        if(newAccessToken!=null){
            tokenManager.setNewAccessToken(accessToken = newAccessToken)
            AppUtils.authChannel.trySend(RequestChannel.TOKEN_REFRESHED)
        }
        else{
            tokenManager.clearTokens()
            AppUtils.authChannel.trySend(RequestChannel.LOG_OUT)
        }
    }

    fun handleLogout(context: Context) {
        tokenManager.clearTokens()
        val intent = Intent(context, OnBoardingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }


}