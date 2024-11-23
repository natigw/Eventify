package com.example.eventify.data.remote.model.events


import com.google.gson.annotations.SerializedName

data class ResponseAllEvents(
    @SerializedName("event")
    val event: Event,
    @SerializedName("location")
    val location: Location
)