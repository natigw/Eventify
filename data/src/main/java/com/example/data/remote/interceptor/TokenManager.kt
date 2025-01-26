package com.example.data.remote.interceptor

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.domain.repository.AuthRepository
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

            return newAccessToken
        } catch (e: Exception) {
            null
        }
    }

    suspend fun checkIfTokenValid() : Boolean{
        try {
            val response = authRepository.verifyUserToken()
            return response
        }
        catch (e : Exception){
            throw e
        }
    }

    fun setNewAccessToken(accessToken : String){
        sharedPrefUserTokens.edit{
            putString("access_token",accessToken)
        }
    }



    fun clearTokens(){
        sharedPrefUserTokens.edit().clear().apply()
    }

}