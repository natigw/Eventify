package com.example.data.remote.thirdpartyregister


import com.google.gson.annotations.SerializedName

data class RequestSignInWithGoogle(
    @SerializedName("id")
    val id: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("display_name")
    val displayName: String,
    @SerializedName("picture")
    val picture: String,
    @SerializedName("provider")
    val provider: String
)