package com.example.data.remote.interceptor

import android.util.Log
import com.example.common.utils.AppUtils
import com.example.common.utils.RequestChannel
import kotlinx.coroutines.delay
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.internal.http2.ConnectionShutdownException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject


class RetryInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        var attempt = 0
        var response = chain.proceed(request)
        val delay = 2000L
        val maxRetries = 3
        Log.e("MYInterceptor", "url: ${request.url()} / attempt: $attempt")

        while (!response.isSuccessful && attempt < maxRetries) {
            Thread.sleep(delay)
            attempt++
            Log.e("MYInterceptor", "url: ${request.url()} / attempt: $attempt")
            response.close()
            response = chain.proceed(request)
            Thread.sleep(delay)
        }

        if (!response.isSuccessful) {
            AppUtils.authChannel.trySend(RequestChannel.ON_401_ERROR)
        }

        return response
    }

}
