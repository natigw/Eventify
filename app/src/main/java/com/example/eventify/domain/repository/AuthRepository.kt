package com.example.eventify.domain.repository

import com.example.eventify.data.remote.model.register.ResponseUserRegistration

interface AuthRepository {
    suspend fun registerUser(
        firstname: String,
        lastname: String,
        username: String,
        email: String,
        password: String
    ): ResponseUserRegistration
}