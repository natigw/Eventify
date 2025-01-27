package com.example.data.remote.model.auth.userToken


import com.google.gson.annotations.SerializedName

data class ResponseVerifyToken(
    @SerializedName("is_authenticated")
    val isAuthenticated: Boolean
)