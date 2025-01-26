package com.example.domain.repository

import com.example.domain.model.auth.SuccessfulUserTokenItem
import com.example.domain.model.auth.UserData
import com.example.domain.model.auth.UserRegistrationItem
import retrofit2.http.Query

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
    ): Boolean

    suspend fun refreshAccessToken(
        refreshToken: String
    ): SuccessfulUserTokenItem

    suspend fun resetUserPassword(
        userEmail : String
    ) : Boolean

    suspend fun signInWithGoogle() : SuccessfulUserTokenItem

    suspend fun resendVerification(
        userEmail: String
    ) : Boolean

    suspend fun isUserVerified(
        userEmail : String
    ) : Boolean

    suspend fun getUserData() : UserData
}