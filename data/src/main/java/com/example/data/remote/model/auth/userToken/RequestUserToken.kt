package com.example.data.remote.model.auth.userToken


import com.google.gson.annotations.SerializedName

data class RequestUserToken(
    @SerializedName("password")
    val password: String,
    @SerializedName("username")
    val username: String
)