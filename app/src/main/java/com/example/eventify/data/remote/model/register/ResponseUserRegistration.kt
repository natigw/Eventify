package com.example.eventify.data.remote.model.register

import com.google.gson.annotations.SerializedName

data class ResponseUserRegistration(
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int
)