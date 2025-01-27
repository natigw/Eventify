package com.example.data.remote.model.auth.userToken

import com.google.gson.annotations.SerializedName

data class RequestResetPassword (
    @SerializedName("email") val userEmail : String
)