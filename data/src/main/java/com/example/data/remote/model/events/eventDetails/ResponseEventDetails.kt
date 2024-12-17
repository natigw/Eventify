package com.example.data.remote.model.events.eventDetails


import com.google.gson.annotations.SerializedName

data class ResponseEventDetails(
    @SerializedName("event")
    val event: Event,
    @SerializedName("location")
    val location: Location
)