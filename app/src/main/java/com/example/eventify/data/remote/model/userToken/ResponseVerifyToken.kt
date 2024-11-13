package com.example.eventify.data.remote.model.userToken


import com.google.gson.annotations.SerializedName

data class ResponseVerifyToken(
    @SerializedName("message")
    val message: String
)