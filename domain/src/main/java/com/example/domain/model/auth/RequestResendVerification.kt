package com.example.domain.model.auth

import com.google.gson.annotations.SerializedName

data class RequestResendVerification (
    @SerializedName("email") val userEmail : String
)