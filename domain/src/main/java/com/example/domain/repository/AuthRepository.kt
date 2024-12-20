package com.example.domain.repository

import com.example.domain.model.SuccessfulUserTokenItem
import com.example.domain.model.UserRegistrationItem

interface AuthRepository {

    suspend fun registerUser(
        firstname: String,
        lastname: String,
        username: String,
        email: String,
        password: String
    ): UserRegistrationItem

    suspend fun loginUser(
        username: String,
        password: String
    ): SuccessfulUserTokenItem

    suspend fun verifyUserToken(
        token: String
    ): Boolean

    suspend fun refreshAccessToken(
        refreshToken: String
    ): SuccessfulUserTokenItem

    suspend fun resetUserPassword(
        userEmail : String
    ) : Boolean

}