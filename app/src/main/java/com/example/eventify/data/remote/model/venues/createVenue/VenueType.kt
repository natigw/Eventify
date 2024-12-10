package com.example.eventify.data.remote.model.venues.createVenue

import com.google.gson.annotations.SerializedName

enum class VenueType(val type: String) {
    @SerializedName("museum")
    MUSEUM("museum"),

    @SerializedName("theatre")
    THEATRE("theatre"),

    @SerializedName("library")
    LIBRARY("library"),

    @SerializedName("cinema")
    CINEMA("cinema"),

    @SerializedName("comedy_club")
    COMEDY_CLUB("comedy_club"),

    @SerializedName("monument")
    MONUMENT("monument"),

    @SerializedName("cultural_space")
    CULTURAL_SPACE("cultural_space");
}
