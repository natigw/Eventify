package com.example.eventify.data.remote.interceptor

import android.content.SharedPreferences
import com.example.eventify.domain.repository.AuthRepository
import okhttp3.Interceptor
import okhttp3.Response

//class AuthInterceptor : Interceptor {
//    override fun intercept(chain: Interceptor.Chain): Response {
//        var request = chain.request()
//
//
//        val accessToken = sharedPrefUserTokens.getString("access_token",null)
//        val tokenType = "Bearer"
//        // SHARED PREF VALUES
//        // 1) access_token
//        // 2) refresh_token
//        // 3) token_type // baxariq buna
//
//        request = request.newBuilder()
//            .addHeader("Authorization","$tokenType $accessToken")
//            .build()
//
//        val response = chain.proceed(request)
//
//        if(response.code() == 401){
//            synchronized(this){
//                val refreshToken = sharedPrefUserTokens.getString("refresh_token",null)
//
//
//
//                try {
//                    val refreshResponse = authRepository.refreshAccessToken().
//                }
//                catch (e : Exception){
//                    //Log out
//                }
//
//            }
//        }
//
//
//    }
//}