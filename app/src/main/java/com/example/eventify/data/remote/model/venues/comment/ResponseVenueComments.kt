package com.example.eventify.data.remote.model.venues.comment


import com.google.gson.annotations.SerializedName

data class ResponseVenueComments(
    @SerializedName("content")
    val content: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("owner_id")
    val ownerId: Int,
    @SerializedName("venue")
    val venue: Int
)