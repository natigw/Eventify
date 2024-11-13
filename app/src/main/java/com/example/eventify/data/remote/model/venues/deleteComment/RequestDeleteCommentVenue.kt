package com.example.eventify.data.remote.model.venues.deleteComment


import com.google.gson.annotations.SerializedName

data class RequestDeleteCommentVenue(
    @SerializedName("id")
    val id: Int
)