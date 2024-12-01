package com.example.eventify.domain.repository

import com.example.eventify.data.remote.model.register.ResponseUserRegistration
import com.example.eventify.data.remote.model.userToken.ResponseSuccessfulUserToken

interface AuthRepository {
    suspend fun registerUser(
        firstname: String,
        lastname: String,
        username: String,
        email: String,
        password: String
    ): ResponseUserRegistration

    suspend fun loginUser(
        username: String,
        password: String
    ) : ResponseSuccessfulUserToken
}