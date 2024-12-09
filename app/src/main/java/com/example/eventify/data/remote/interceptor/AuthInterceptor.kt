package com.example.eventify.data.remote.interceptor

import android.content.SharedPreferences
import com.example.eventify.common.utils.AppUtils
import com.example.eventify.common.utils.RequestChannel
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


        val temp = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJibHVlc3RlZWwyIiwiaWQiOjM0LCJleHAiOjE3MzM3ODA4OTF9.AMEQM0pvozwZvdicBXJfRxnGLP-kWSsN7cJkNAWvc9Y"
        request = request.newBuilder()
            .addHeader("Authorization","$tokenType $temp")
            .build()

        val response = chain.proceed(request)

        if(response.code() == 401){
            AppUtils.authChannel.trySend(RequestChannel.ON_401_ERROR)
        }

        return response
    }
}