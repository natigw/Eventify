package com.example.data.remote.model.events.comment.addComment


import com.google.gson.annotations.SerializedName

data class RequestAddEventComment(
    @SerializedName("content")
    val content: String,
    @SerializedName("event")
    val event: Int
)