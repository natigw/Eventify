package com.example.data.remote.model.auth.register


import com.google.gson.annotations.SerializedName

data class RequestUserRegistration(
    @SerializedName("email")
    val email: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("is_organizer")
    val isOrganizer: Int,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("username")
    val username: String
)