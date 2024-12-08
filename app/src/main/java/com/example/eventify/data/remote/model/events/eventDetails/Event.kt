package com.example.eventify.data.remote.model.events.eventDetails


import com.google.gson.annotations.SerializedName

data class Event(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("event_type")
    val eventType: String,
    @SerializedName("finish")
    val finish: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("num_likes")
    val numLikes: Int,
    @SerializedName("organizer_id")
    val organizerId: Int,
    @SerializedName("poster_image_link")
    val posterImageLink: String,
    @SerializedName("start")
    val start: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("venue_id")
    val venueId: Int
)