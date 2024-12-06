package com.example.eventify.data.remote.interceptor

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.eventify.common.utils.AppUtils
import com.example.eventify.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Named


class TokenManager @Inject constructor(
    @Named("UserTokens") private val  sharedPrefUserTokens: SharedPreferences,
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
            val response = authRepository.refreshAccessToken(refreshToken.toString())

            val newAccessToken = response.accessToken
            val newRefreshToken = response.refreshToken

            sharedPrefUserTokens.edit {
                putString("access_token", newAccessToken)
                putString("refresh_token", newRefreshToken)
                putString("token_type",response.tokenType)
            }

            return newAccessToken
        } catch (e: Exception) {
            AppUtils.authChannel.trySend(Unit)
            sharedPrefUserTokens.edit {
                clear()
            }
            null
        }
    }

    fun clearTokens(){
        sharedPrefUserTokens.edit().clear().apply()
    }

}