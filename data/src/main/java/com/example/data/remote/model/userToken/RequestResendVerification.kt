package com.example.data.remote.model.userToken

import com.google.gson.annotations.SerializedName

data class RequestResendVerification (
    @SerializedName("email") val userEmail : String
)