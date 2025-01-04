package com.example.data.remote.interceptor

import android.content.SharedPreferences
import android.util.Log
import com.example.common.utils.AppUtils
import com.example.common.utils.RequestChannel
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Named


class AuthInterceptor @Inject constructor(
    @Named("UserTokens") private val sharedPrefUserTokens: SharedPreferences,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        val accessToken = sharedPrefUserTokens.getString("access_token","")
        val tokenType = "Bearer"
        Log.e("MYInterceptor","url :  ${request.url()}")
        request = request.newBuilder()
            .addHeader("Authorization","$tokenType $accessToken")
            .build()


        val response = chain.proceed(request)
        if(response.code() == 401){
            AppUtils.authChannel.trySend(RequestChannel.ON_401_ERROR)
        }

        return response
    }
}