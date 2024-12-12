package com.example.eventify.data.remote.interceptor

import com.example.eventify.common.utils.AppUtils
import com.example.eventify.common.utils.RequestChannel
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
        var response: Response? = null
        val delay = 1000L
        val maxRetries = 3
        var msg = ""

        val mediaType = MediaType.parse("application/json")

        while (attempt < maxRetries) {
            try {
                AppUtils.authChannel.trySend(RequestChannel.LOG_OUT)
                response = chain.proceed(request)

                // Check if the response is successful before proceeding
                if (response.isSuccessful) {
                    return response
                }

            } catch (e: Exception) {
                attempt++
                Thread.sleep(delay)
                e.printStackTrace()

                when (e) {
                    is SocketTimeoutException -> {
                        msg = "Timeout - Please check your internet connection"
                    }
                    is UnknownHostException -> {
                        msg = "Unable to make a connection. Please check your internet"
                    }
                    is ConnectionShutdownException -> {
                        msg = "Connection shutdown. Please check your internet"
                    }
                    is IOException -> {
                        msg = "Server is unreachable, please try again later."
                    }
                    is IllegalStateException -> {
                        msg = "${e.message}"
                    }
                    else -> {
                        msg = "${e.message}"
                    }
                }
            } finally {
                response?.close()
            }
        }

        val errorContent = "{ \"error\": \"$msg\" }"
        val responseBody: ResponseBody = ResponseBody.create(mediaType, errorContent)

        // Return a custom response after retry attempts are exhausted
        return Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(999) // Custom error code
            .message(msg)
            .body(responseBody)
            .build()
    }
}
