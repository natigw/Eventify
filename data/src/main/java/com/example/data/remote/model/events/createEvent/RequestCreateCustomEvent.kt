package com.example.data.remote.model.events.createEvent

import com.google.gson.annotations.SerializedName

data class RequestCreateCustomEvent(
    @SerializedName("date")
    val date: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("event_type")
    val eventType: String,
    @SerializedName("finish")
    val finish: String,
    @SerializedName("lat")
    val lat: String,
    @SerializedName("lng")
    val lng: String,
    @SerializedName("poster_image_link")
    val posterImageLink: String,
    @SerializedName("start")
    val start: String,
    @SerializedName("title")
    val title: String
)