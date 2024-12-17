package com.example.data.remote.model.userToken

import com.google.gson.annotations.SerializedName
import java.io.Serial

data class RequestPasswordReset(
    @SerializedName("email") val userEmail : String
)