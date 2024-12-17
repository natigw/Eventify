package com.example.data.remote.model.venues.comment.addComment


import com.google.gson.annotations.SerializedName

data class Owner(
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("is_organizer")
    val isOrganizer: Boolean,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("username")
    val username: String
)