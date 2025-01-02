package com.example.domain.model.auth


import com.google.gson.annotations.SerializedName

data class SuccessfulUserTokenItem(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("token_type")
    val tokenType: String
)
