package com.example.data.remote.model.events.search


import com.google.gson.annotations.SerializedName

data class ResponseSearchEvent(
    @SerializedName("event")
    val event: Event,
    @SerializedName("location")
    val location: Location
)