package com.example.data.remote.model.events.likeDislike.favEvents


import com.google.gson.annotations.SerializedName

data class ResponseFavEvents(
    @SerializedName("event")
    val event: Event,
    @SerializedName("location")
    val location: Location
)