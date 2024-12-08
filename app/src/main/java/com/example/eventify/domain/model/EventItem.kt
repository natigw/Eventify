package com.example.eventify.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EventItem(
    val placeId : Int,
    val name : String,
    val imageLink : String?,
    val description : String,
    val eventType: String,
    val organizer: String,
    val publishingDate: String,
    val eventDate: String,
    val eventDurationHours: String,
    val likeCount: Int,
    val lat : Double,
    val lng : Double
) : Parcelable