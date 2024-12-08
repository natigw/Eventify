package com.example.eventify.data.remote.model.events.eventDetails


import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("lat")
    val lat: String,
    @SerializedName("lng")
    val lng: String
)