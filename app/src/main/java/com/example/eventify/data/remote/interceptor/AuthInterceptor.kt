package com.example.eventify.data.remote.interceptor

import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val sharedPrefUserTokens: SharedPreferences,
    private val onUnAuthorized : ()-> Unit
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()


        val accessToken = sharedPrefUserTokens.getString("access_token",null)
        val tokenType = "Bearer"


        request = request.newBuilder()
            .addHeader("Authorization","$tokenType $accessToken")
            .build()

        val response = chain.proceed(request)

        if(response.code() == 401){
            onUnAuthorized()
        }

        return response
    }
}