package com.example.data.remote.model.events.search


import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("lat")
    val lat: String,
    @SerializedName("lng")
    val lng: String
)