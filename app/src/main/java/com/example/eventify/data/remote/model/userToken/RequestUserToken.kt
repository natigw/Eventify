package com.example.eventify.data.remote.model.userToken


import com.google.gson.annotations.SerializedName

data class RequestUserToken(
    @SerializedName("password")
    val password: String,
    @SerializedName("username")
    val username: String
)