package com.example.eventify.data.remote.model.events.likeDislike


import com.google.gson.annotations.SerializedName

data class RequestLikeDislikeEvent(
    @SerializedName("venue")
    val venue: Int
)