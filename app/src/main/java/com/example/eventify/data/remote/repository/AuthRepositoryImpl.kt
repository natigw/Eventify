package com.example.eventify.data.remote.repository

import com.example.eventify.data.remote.api.AuthAPI
import com.example.eventify.data.remote.model.register.RequestUserRegistration
import com.example.eventify.data.remote.model.register.ResponseUserRegistration
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
        return response.body()!!
    }
}