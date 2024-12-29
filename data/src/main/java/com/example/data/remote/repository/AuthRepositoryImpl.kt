package com.example.data.remote.repository

import com.example.data.remote.api.AuthAPI
import com.example.data.remote.model.register.RequestUserRegistration
import com.example.data.remote.model.userToken.RequestResetPassword
import com.example.domain.model.auth.SuccessfulUserTokenItem
import com.example.domain.model.auth.UserRegistrationItem
import com.example.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    val api: AuthAPI
) : AuthRepository {

    override suspend fun registerUser(
        firstname: String,
        lastname: String,
        username: String,
        email: String,
        password: String
    ): UserRegistrationItem {

        try {
            val response = api.registerUser(
                RequestUserRegistration(
                    username = username,
                    email = email,
                    password = password,
                    firstName = firstname,
                    lastName = lastname,
                    isOrganizer = 0
                )
            )
            if(response.isSuccessful && response.body() != null){
                return UserRegistrationItem(
                    message = response.body()!!.message,
                    status = response.body()!!.status
                )
            }
            else{
                throw Exception("Error ${response.code()}: ${response.errorBody()?.string()}")
            }

        }
        catch (e: Exception){
            throw e
        }

    }

    override suspend fun loginUser(
        username: String,
        password: String
    ) : SuccessfulUserTokenItem {
        try {
            val response = api.loginUser(
                username = username,
                password = password)

            if(response.isSuccessful && response.body() != null){
                val accessToken = response.body()!!.accessToken
                val refreshToken =response.body()!!.refreshToken
                val tokenType = response.body()!!.tokenType

                return SuccessfulUserTokenItem(
                    accessToken = accessToken,
                    refreshToken = refreshToken,
                    tokenType = tokenType
                )
            }
            else{
                throw Exception(response.errorBody()?.string())
            }
        }
        catch (e : Exception){
            throw e
        }
    }

    override suspend fun verifyUserToken(token: String): Boolean {

        try {
            val response = api.verifyUserToken(token)

            if(response.isSuccessful && response.body() != null){
                   return response.body()!!.isAuthenticated
            }
            return false
        }
        catch (e : Exception){
            throw e
        }
    }

    override suspend fun refreshAccessToken(
        refreshToken: String
    ): SuccessfulUserTokenItem {
        try {
            val response = api.refreshAccessToken(refreshToken)

            if(response.isSuccessful && response.body() != null){
                return SuccessfulUserTokenItem(
                    accessToken = response.body()!!.accessToken,
                    refreshToken = response.body()!!.refreshToken,
                    tokenType = response.body()!!.tokenType
                )
            }
            else{
                throw Exception()
            }
        }
        catch (e : Exception){
            throw e
        }
    }

    override suspend fun resetUserPassword(userEmail: String): Boolean {
        return try {
            api.resetUserPassword(RequestResetPassword(userEmail))
            true
        }
        catch (e : Exception){
            false
        }
    }

}