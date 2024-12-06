package com.example.eventify.data.remote.interceptor

import android.content.SharedPreferences
import com.example.eventify.common.utils.AppUtils
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Named


//lazimsiz gorsenir cunki refresh token authorization istemir !!!!!!!!!!!!!!!!!!!!!!!
class AuthInterceptor @Inject constructor(
    @Named("UserTokens") private val sharedPrefUserTokens: SharedPreferences,
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
            AppUtils.authChannel.trySend(Unit)
        }

        return response
    }
}