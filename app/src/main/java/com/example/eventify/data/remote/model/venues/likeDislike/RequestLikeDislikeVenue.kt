package com.example.eventify.data.remote.model.venues.likeDislike


import com.google.gson.annotations.SerializedName

data class RequestLikeDislikeVenue(
    @SerializedName("venue")
    val venue: Int
)