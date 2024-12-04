package com.example.eventify.data.remote.model.userToken


import com.google.gson.annotations.SerializedName

data class ResponseSuccessfulUserToken(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("token_type")
    val tokenType: String
)