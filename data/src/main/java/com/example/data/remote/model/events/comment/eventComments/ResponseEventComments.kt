package com.example.data.remote.model.events.comment.eventComments


import com.google.gson.annotations.SerializedName

data class ResponseEventComments(
    @SerializedName("comment")
    val comment: Comment,
    @SerializedName("owner")
    val owner: Owner
)