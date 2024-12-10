package com.example.eventify.data.remote.model.venues.allVenues


import com.google.gson.annotations.SerializedName

data class ResponseAllVenues(
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image_1_link")
    val image1Link: String,
    @SerializedName("image_2_link")
    val image2Link: Any,
    @SerializedName("image_3_link")
    val image3Link: Any,
    @SerializedName("lat")
    val lat: String,
    @SerializedName("lng")
    val lng: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("num_likes")
    val numLikes: Int,
    @SerializedName("venue_type")
    val venueType: String,
    @SerializedName("work_hours_close")
    val workHoursClose: String,
    @SerializedName("work_hours_open")
    val workHoursOpen: String
)