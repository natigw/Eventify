package com.example.eventify.data.remote.model.venues.addComment


import com.google.gson.annotations.SerializedName

data class RequestAddCommentVenue(
    @SerializedName("content")
    val content: String,
    @SerializedName("venue")
    val venue: Int
)