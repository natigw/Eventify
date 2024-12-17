package com.example.data.remote.model.venues.createVenue


import com.google.gson.annotations.SerializedName

data class RequestCreateVenue(
    @SerializedName("description")
    val description: String,
    @SerializedName("lat")
    val lat: String,
    @SerializedName("lng")
    val lng: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("venue_type")
    val venueType: String,
    @SerializedName("work_hours_close")
    val workHoursClose: String,
    @SerializedName("work_hours_open")
    val workHoursOpen: String
)