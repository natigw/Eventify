package com.example.data.remote.model.venues.comment.addComment


import com.google.gson.annotations.SerializedName

data class RequestAddVenueComment(
    @SerializedName("content")
    val content: String,
    @SerializedName("venue")
    val venue: Int
)