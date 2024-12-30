package com.example.data.remote.interceptor

import android.util.Log
import com.example.common.utils.AppUtils
import com.example.common.utils.RequestChannel
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
        val delay = 1000L
        val maxRetries = 3



        while (attempt < maxRetries) {
            try {
                response = chain.proceed(request)

                if (response.isSuccessful) {
                    return response
                }



            } catch (e: Exception) {
                e.printStackTrace()

            }
            attempt++
            Thread.sleep(delay*attempt)

        }
        response.close()
        AppUtils.authChannel.trySend(RequestChannel.ON_401_ERROR)

        Log.e("main",response.toString())

        return response

    }
}
