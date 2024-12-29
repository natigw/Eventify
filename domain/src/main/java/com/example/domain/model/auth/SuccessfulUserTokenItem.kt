package com.example.domain.model.auth

data class SuccessfulUserTokenItem(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String
)
