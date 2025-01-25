package com.example.data.remote.model.events.comment.eventComments


import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("content")
    val content: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("event")
    val event: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("owner_id")
    val ownerId: Int
)