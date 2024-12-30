package com.example.data.remote.thirdpartyregister


import com.google.gson.annotations.SerializedName

data class RequestSignInWithGoogle(
    @SerializedName("display_name")
    val displayName: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("picture")
    val picture: String,
    @SerializedName("provider")
    val provider: String
)