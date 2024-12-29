package com.example.data.remote.model.events.likeDislike


import com.google.gson.annotations.SerializedName

data class RequestLikeDislikeEvent(
    @SerializedName("event")
    val event: Int
)