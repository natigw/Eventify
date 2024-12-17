package com.example.data.remote.model.events.allEvents


import com.google.gson.annotations.SerializedName

data class ResponseAllEvents(
    @SerializedName("event")
    val event: Event,
    @SerializedName("location")
    val location: Location
)