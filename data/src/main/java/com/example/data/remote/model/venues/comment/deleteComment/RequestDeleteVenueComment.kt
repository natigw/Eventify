package com.example.data.remote.model.venues.comment.deleteComment


import com.google.gson.annotations.SerializedName

data class RequestDeleteVenueComment(
    @SerializedName("id")
    val id: Int
)