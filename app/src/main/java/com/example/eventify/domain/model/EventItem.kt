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
    val organizerId: Int,
    val publishingDate: String,
    val eventDate: String,
    val startHour: String,
    val endHour: String,
    val likeCount: Int,
    val latCoordinate : String,
    val lngCoordinate : String
) : Parcelable