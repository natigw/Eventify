package com.example.eventify.data.remote.interceptor

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.eventify.domain.repository.AuthRepository
import javax.inject.Inject



class TokenManager @Inject constructor(
    private val  sharedPrefUserTokens: SharedPreferences,
    private val authRepository: AuthRepository
)
{
    fun getValidAccessToken() : String?{
        val value = sharedPrefUserTokens.getString("access_token",null)
        return value
    }

    suspend fun refreshToken(): String? {
        val refreshToken = sharedPrefUserTokens.getString("refresh_token", null)
        return try {
            val response = authRepository.refreshAccessToken("Bearer $refreshToken")

            val newAccessToken = response.accessToken
            val newRefreshToken = response.refreshToken

            sharedPrefUserTokens.edit {
                putString("access_token", newAccessToken)
                putString("refresh_token", newRefreshToken)
            }

            return newAccessToken
        } catch (e: Exception) {
            null
        }
    }
}