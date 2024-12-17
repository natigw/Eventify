package com.example.domain.model

data class SuccessfulUserTokenItem(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String
)
