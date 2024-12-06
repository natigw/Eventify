package com.example.eventify.data.remote.repository

import com.example.eventify.data.remote.api.AuthAPI
import com.example.eventify.data.remote.model.register.RequestUserRegistration
import com.example.eventify.data.remote.model.register.ResponseUserRegistration
import com.example.eventify.data.remote.model.userToken.ResponseSuccessfulUserToken
import com.example.eventify.data.remote.model.userToken.ResponseVerifyToken
import com.example.eventify.domain.repository.AuthRepository
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
    ): ResponseUserRegistration {

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
                return response.body()!!
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
    ) : ResponseSuccessfulUserToken {
        try {
            val response = api.loginUser(
                username = username,
                password = password)
            if(response.isSuccessful && response.body() != null){
                return response.body()!!
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
    ): ResponseSuccessfulUserToken {
        try {
            val response = api.refreshAccessToken(refreshToken)

            if(response.isSuccessful && response.body() != null){
                return response.body()!!
            }
            else{
                throw Exception()
            }
        }
        catch (e : Exception){
            throw e
        }
    }

}