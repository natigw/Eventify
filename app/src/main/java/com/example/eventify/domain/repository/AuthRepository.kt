package com.example.eventify.domain.repository

import com.example.eventify.data.remote.model.register.ResponseUserRegistration

interface AuthRepository {
    suspend fun registerUser(): ResponseUserRegistration
}