package com.example.eventify

import android.content.Context
import android.content.Intent
import com.example.common.utils.AppUtils
import com.example.common.utils.RequestChannel
import com.example.data.remote.interceptor.TokenManager
import com.example.eventify.presentation.ui.activities.OnBoardingActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object NetworkUtils {
    private lateinit var tokenManager: TokenManager

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
        NetworkUtils.tokenManager = tokenManager
    }


    suspend fun handleInvalidAccessToken() : Boolean{
        val checkIfValid =  try {
            tokenManager.checkIfTokenValid()
        }
        catch (_:Exception){
            tokenManager.clearTokens()
            AppUtils.authChannel.trySend(RequestChannel.LOG_OUT)
        }

        if(checkIfValid == true){
            return true
        }
        else if(checkIfValid == false){
            val newAccessToken = tokenManager.refreshToken()
            if(newAccessToken != null){
                tokenManager.setNewAccessToken(accessToken = newAccessToken)
                return true
            }
            else{
                tokenManager.clearTokens()
                AppUtils.authChannel.trySend(RequestChannel.LOG_OUT)
            }
            return false
        }
        else{
            tokenManager.clearTokens()
            return false
        }
    }

    fun handleLogout(context: Context) {
        val currentUser = Firebase.auth.currentUser
        if(currentUser!=null){
            Firebase.auth.signOut()
        }
        tokenManager.clearTokens()
        val intent = Intent(context, OnBoardingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }
}