package com.example.data.remote.model.events.createEvent

import com.google.gson.annotations.SerializedName

enum class EventType(val type: String) {
    @SerializedName("theatre")
    THEATRE("theatre"),

    @SerializedName("concert")
    CONCERT("concert"),

    @SerializedName("exhibition")
    EXHIBITION("exhibition"),

    @SerializedName("book_fare")
    BOOK_FARE("book_fare"),

    @SerializedName("seminar")
    SEMINAR("seminar"),

    @SerializedName("festival")
    FESTIVAL("festival"),

    @SerializedName("dance")
    DANCE("dance")
}