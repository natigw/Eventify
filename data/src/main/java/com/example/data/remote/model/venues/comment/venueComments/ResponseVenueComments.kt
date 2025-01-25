package com.example.data.remote.model.venues.comment.venueComments


import com.google.gson.annotations.SerializedName

data class ResponseVenueComments(
    @SerializedName("comment")
    val comment: Comment,
    @SerializedName("owner")
    val owner: Owner
)