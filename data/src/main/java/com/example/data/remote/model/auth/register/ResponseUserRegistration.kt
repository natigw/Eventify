package com.example.data.remote.model.auth.register

import com.google.gson.annotations.SerializedName

data class ResponseUserRegistration(
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int
)